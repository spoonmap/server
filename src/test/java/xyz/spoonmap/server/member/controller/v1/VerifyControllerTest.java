package xyz.spoonmap.server.member.controller.v1;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import xyz.spoonmap.server.member.dto.response.SignupResponse;
import xyz.spoonmap.server.member.service.v1.MemberServiceV1;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(VerifyController.class)
class VerifyControllerTest {

    MockMvc mockMvc;

    @Autowired
    VerifyController controller;

    @MockBean
    MemberServiceV1 memberService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .alwaysDo(print())
                                 .build();
    }

    @DisplayName("이메일 인증")
    @Test
    void verifyEmail() throws Exception {
        Long id = 1L;
        String email = "email@email.com";

        SignupResponse signupResponse = new SignupResponse(id, email);
        given(memberService.verify(anyLong())).willReturn(signupResponse);

        mockMvc.perform(get("/members/verify?code={id}", id)
                   .characterEncoding(UTF_8))
               .andExpect(status().is3xxRedirection())
               .andExpect(header().exists(HttpHeaders.LOCATION));

        then(memberService).should(times(1)).verify(id);
    }

}
