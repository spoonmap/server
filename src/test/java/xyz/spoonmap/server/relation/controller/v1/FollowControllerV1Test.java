package xyz.spoonmap.server.relation.controller.v1;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import xyz.spoonmap.server.member.dto.response.MemberResponse;
import xyz.spoonmap.server.relation.dto.request.FollowRequest;
import xyz.spoonmap.server.relation.dto.response.FollowAddResponse;
import xyz.spoonmap.server.relation.dto.response.FollowResponse;
import xyz.spoonmap.server.relation.service.RelationService;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(FollowControllerV1.class)
class FollowControllerV1Test {

    MockMvc mockMvc;

    @Autowired
    FollowControllerV1 controller;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RelationService relationService;

    @Autowired
    WebApplicationContext context;

    UserDetails userDetails;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                                 .apply(springSecurity())
                                 .defaultResponseCharacterEncoding(UTF_8)
                                 .alwaysDo(print())
                                 .build();

        userDetails = mock(UserDetails.class);
    }

    @Test
    @DisplayName("팔로우 추가")
    void testAddFollow() throws Exception {
        Long senderId = 1L;
        Long targetId = 2L;
        FollowRequest followRequest = new FollowRequest(targetId);
        FollowAddResponse followAddResponse = new FollowAddResponse(senderId, targetId);

        String content = objectMapper.writeValueAsString(followRequest);
        given(relationService.requestFollow(any(UserDetails.class), anyLong())).willReturn(followAddResponse);

        mockMvc.perform(post("/v1/members/follows")
                   .with(user(userDetails))
                   .content(content)
                   .contentType(APPLICATION_JSON)
                   .characterEncoding(UTF_8)
                   .with(csrf()))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.code", is(CREATED.value())))
               .andExpect(jsonPath("$.data.senderId", is(senderId), Long.class))
               .andExpect(jsonPath("$.data.receiverId", is(targetId), Long.class));
    }

    @Test
    @DisplayName("팔로우 조회")
    void testRetrieveFollow() throws Exception {
        Long id = 1L;
        int size = 10;

        List<MemberResponse> memberResponses = IntStream.range(0, size)
                                                        .mapToObj(i -> new MemberResponse(Integer.toUnsignedLong(i),
                                                            "name" + i, "nickname" + i,
                                                            null))
                                                        .toList();
        FollowResponse followResponse = new FollowResponse(id, memberResponses);

        given(relationService.retrieveFollows(userDetails)).willReturn(followResponse);

        mockMvc.perform(get("/v1/members/follows")
                   .with(user(userDetails))
                   .characterEncoding(UTF_8))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code", is(OK.value())))
               .andExpect(jsonPath("$.data.follows", hasSize(memberResponses.size())));

        then(relationService).should(times(1)).retrieveFollows(any(UserDetails.class));
    }

    @Test
    @DisplayName("팔로우 수락")
    void testAcceptFollow() throws Exception {

        Long senderId = 1L;
        Long receiverId = 2L;

        FollowRequest followRequest = new FollowRequest(2L);
        FollowAddResponse followAddResponse = new FollowAddResponse(senderId, receiverId);

        String content = objectMapper.writeValueAsString(followRequest);

        given(relationService.acceptFollow(followRequest.targetMemberId(), userDetails)).willReturn(followAddResponse);

        mockMvc.perform(patch("/v1/members/follows")
                   .with(user(userDetails))
                   .contentType(APPLICATION_JSON)
                   .characterEncoding(UTF_8)
                   .content(content)
                   .with(csrf()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code", is(OK.value())))
               .andExpect(jsonPath("$.data.senderId", is(senderId), Long.class))
               .andExpect(jsonPath("$.data.receiverId", is(receiverId), Long.class));

        then(relationService).should(times(1)).acceptFollow(anyLong(), any(UserDetails.class));
    }

    @Test
    @DisplayName("팔로우 요청 목록 조회")
    void testRetrieveFollowRequest() throws Exception {
        Long id = 1L;
        int size = 10;

        List<MemberResponse> memberResponses = IntStream.range(0, size)
                                                        .mapToObj(i -> new MemberResponse(Integer.toUnsignedLong(i),
                                                            "name" + i, "nickname" + i,
                                                            null))
                                                        .toList();
        FollowResponse followResponse = new FollowResponse(id, memberResponses);

        given(relationService.retrieveFollowRequest(userDetails)).willReturn(followResponse);

        mockMvc.perform(get("/v1/members/follows/request")
                   .with(user(userDetails))
                   .characterEncoding(UTF_8))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code", is(OK.value())))
               .andExpect(jsonPath("$.data.follows", hasSize(memberResponses.size())));

        then(relationService).should(times(1)).retrieveFollowRequest(any(UserDetails.class));
    }

    @Test
    @DisplayName("팔로우 거절")
    void testRejectFollow() throws Exception {
        Long senderId = 1L;
        Long receiverId = 2L;

        FollowAddResponse followAddResponse = new FollowAddResponse(senderId, receiverId);

        given(relationService.rejectFollow(senderId, userDetails)).willReturn(followAddResponse);

        mockMvc.perform(patch("/v1/members/follows/rejection/{senderId}", senderId)
                   .with(user(userDetails))
                   .contentType(APPLICATION_JSON)
                   .characterEncoding(UTF_8)
                   .with(csrf()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code", is(OK.value())))
               .andExpect(jsonPath("$.data.senderId", is(senderId), Long.class))
               .andExpect(jsonPath("$.data.receiverId", is(receiverId), Long.class));

        then(relationService).should(times(1)).rejectFollow(anyLong(), any(UserDetails.class));
    }

}
