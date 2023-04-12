package xyz.spoonmap.server.relation.controller.v1;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import xyz.spoonmap.server.relation.dto.response.FollowResponse;
import xyz.spoonmap.server.relation.dto.response.FollowerResponse;
import xyz.spoonmap.server.relation.service.RelationService;
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(FollowerControllerV1.class)
class FollowerControllerV1Test {

    MockMvc mockMvc;

    @Autowired
    FollowerControllerV1 controller;

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
    @DisplayName("팔로워 조회")
    void testRetrieveFollower() throws Exception {
        Long id = 1L;
        int size = 10;

        List<MemberResponse> memberResponses = IntStream.range(0, size)
                                                        .mapToObj(i -> new MemberResponse(Integer.toUnsignedLong(i),
                                                            "name" + i, "nickname" + i,
                                                            null))
                                                        .toList();
        FollowerResponse followerResponse = new FollowerResponse(id, memberResponses);

        given(relationService.retrieveFollowers(userDetails)).willReturn(followerResponse);

        mockMvc.perform(get("/v1/members/followers")
                   .with(user(userDetails))
                   .characterEncoding(UTF_8))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code", is(OK.value())))
               .andExpect(jsonPath("$.data.followers", hasSize(memberResponses.size())));

        then(relationService).should(times(1)).retrieveFollowers(userDetails);
    }


    @Test
    @DisplayName("팔로워 요청 목록 조회")
    void testRetrieveFollowerRequest() throws Exception {
        Long id = 1L;
        int size = 10;

        List<MemberResponse> memberResponses = IntStream.range(0, size)
                                                        .mapToObj(i -> new MemberResponse(Integer.toUnsignedLong(i),
                                                            "name" + i, "nickname" + i,
                                                            null))
                                                        .toList();
        FollowerResponse followerResponse = new FollowerResponse(id, memberResponses);

        given(relationService.retrieveFollowerRequest(userDetails)).willReturn(followerResponse);

        mockMvc.perform(get("/v1/members/followers/request")
                   .with(user(userDetails))
                   .characterEncoding(UTF_8))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code", is(OK.value())))
               .andExpect(jsonPath("$.data.followers", hasSize(memberResponses.size())));

        then(relationService).should(times(1)).retrieveFollowerRequest(any(UserDetails.class));
    }

}
