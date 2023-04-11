package xyz.spoonmap.server.relation.controller.v1;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import xyz.spoonmap.server.member.dto.request.SignupRequest;
import xyz.spoonmap.server.member.dto.response.EmailResponse;
import xyz.spoonmap.server.member.dto.response.PasswordUpdateResponse;
import xyz.spoonmap.server.member.dto.response.SignupResponse;
import xyz.spoonmap.server.member.service.v1.MemberServiceV1;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import xyz.spoonmap.server.relation.dto.request.FollowRequest;
import xyz.spoonmap.server.relation.dto.response.FollowAddResponse;
import xyz.spoonmap.server.relation.service.RelationService;
import xyz.spoonmap.server.relation.service.v1.RelationServiceV1;

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

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .defaultResponseCharacterEncoding(UTF_8)
                                 .alwaysDo(print())
                                 .build();
    }

    @Test
    @DisplayName("팔로우 추가")
    void testAddFollow() throws Exception {
        Long senderId = 1L;
        Long targetId = 2L;
        FollowRequest followRequest = new FollowRequest(targetId);
        FollowAddResponse followAddResponse = new FollowAddResponse(senderId, targetId);
        UserDetails userDetails = mock(UserDetails.class);

        String content = objectMapper.writeValueAsString(followRequest);
        given(relationService.requestFollow(userDetails, targetId)).willReturn(followAddResponse);

        mockMvc.perform(post("/v1/members/follows")
                   .content(content)
                   .contentType(APPLICATION_JSON)
                   .characterEncoding(UTF_8))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.code", is(OK.value())))
               .andExpect(jsonPath("$.data.senderId", is(senderId)))
               .andExpect(jsonPath("$.data.receiverId", is(targetId)));
    }

    @Test
    @DisplayName("팔로우 조회")
    void testRetrieveFollow() throws Exception {

    }

    @Test
    @DisplayName("팔로우 수락")
    void testAcceptFollow() throws Exception {

    }

    @Test
    @DisplayName("팔로우 요청 목록 조회")
    void testRetrieveFollowRequest() throws Exception {

    }

    @Test
    @DisplayName("팔로우 거절")
    void testRejectFollow() throws Exception {

    }

}
