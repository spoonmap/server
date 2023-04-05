package xyz.spoonmap.server.member.controller.v1;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import xyz.spoonmap.server.member.dto.request.SignupRequest;
import xyz.spoonmap.server.member.dto.response.EmailResponse;
import xyz.spoonmap.server.member.dto.response.PasswordUpdateResponse;
import xyz.spoonmap.server.member.dto.response.SignupResponse;
import xyz.spoonmap.server.member.service.v1.MemberServiceV1;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(MemberControllerV1.class)
class MemberControllerV1Test {

    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberControllerV1 controller;

    @MockBean
    MemberServiceV1 memberService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .defaultResponseCharacterEncoding(UTF_8)
                                 .alwaysDo(print())
                                 .build();
    }

    Long id = 1L;
    String email = "email@email.com";
    String password = "$2a$12$qwNhSzw4uYf2AlwJOn5/letEMrMeNLGCnO6dzmm2pprQWvZubixXe";
    String nickname = "Nick";
    String name = "홍길동";

    @DisplayName("회원가입 요청")
    @Test
    void testSignup() throws Exception {

        SignupRequest request = new SignupRequest(email, password, nickname, name);

        SignupResponse response = new SignupResponse(id, email);

        given(memberService.signUp(any(SignupRequest.class))).willReturn(response);

        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/v1/members/signup")
                   .characterEncoding(UTF_8)
                   .contentType(APPLICATION_JSON)
                   .content(body))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.code", is(201)))
               .andExpect(jsonPath("$.data.email", is(email)));

        then(memberService).should(times(1)).signUp(any(SignupRequest.class));
    }

    @DisplayName("회원탈퇴")
    @Test
    void testWithdraw() throws Exception {
        EmailResponse emailResponse = new EmailResponse(email);
        given(memberService.withdraw(email)).willReturn(emailResponse);

        mockMvc.perform(delete("/v1/members?email={email}", email)
                   .characterEncoding(UTF_8))
               .andExpect(status().isNoContent())
               .andExpect(jsonPath("$.code", is(NO_CONTENT.value())))
               .andExpect(jsonPath("$.data.email", is(email)));
    }

    @DisplayName("이메일 조회")
    @Test
    void testRetrieveMemberByEmail() throws Exception {
        EmailResponse response = new EmailResponse(email);
        given(memberService.retrieveMemberByEmail(email)).willReturn(response);

        mockMvc.perform(get("/v1/members/email/{email}", email)
                   .characterEncoding(UTF_8))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code", is(OK.value())))
               .andExpect(jsonPath("$.data.email", is(email)));

        then(memberService).should(times(1)).retrieveMemberByEmail(email);
    }

    @DisplayName("비밀번호 초기화")
    @Test
    void testFindPassword() throws Exception {
        PasswordUpdateResponse response = new PasswordUpdateResponse(email, password);
        given(memberService.findPassword(email)).willReturn(response);

        mockMvc.perform(patch("/v1/members/password?email={email}", email)
                   .characterEncoding(UTF_8))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code", is(OK.value())))
               .andExpect(jsonPath("$.data.email", is(email)));
    }

}
