package xyz.spoonmap.server.member.controller.v1;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import xyz.spoonmap.server.authentication.CustomUserDetail;
import xyz.spoonmap.server.member.dto.request.PasswordUpdateRequest;
import xyz.spoonmap.server.member.dto.response.EmailResponse;
import xyz.spoonmap.server.member.dto.response.MemberResponse;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.service.MemberProfileService;
import xyz.spoonmap.server.util.MultipartBuilder;
import xyz.spoonmap.server.util.RandomEntityGenerator;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(MemberProfileControllerV1.class)
class MemberProfileControllerV1Test {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberProfileControllerV1 controller;

    @MockBean
    MemberProfileService memberProfileService;

    UserDetails userDetails;

    MemberResponse memberResponse;
    EmailResponse emailResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                                 .apply(springSecurity())
                                 .defaultResponseCharacterEncoding(UTF_8)
                                 .alwaysDo(print())
                                 .build();

        Member member = RandomEntityGenerator.createWithId(Member.class);
        userDetails = new CustomUserDetail(member);
        memberResponse = new MemberResponse(1L, "name", "nickname", "test");
        emailResponse = new EmailResponse("test@test.com");
    }

    @Test
    @DisplayName("닉네임으로 회원 조회")
    void testRetrieveByNickname() throws Exception {
        String nickname = "nickname";

        given(memberProfileService.retrieveByNickname(nickname)).willReturn(memberResponse);

        mockMvc.perform(get("/v1/members/profile/nickname/{nickname}", nickname)
                   .with(user(userDetails)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code", is(OK.value())))
               .andExpect(jsonPath("$.data.nickname", is(nickname)));

        then(memberProfileService).should(times(1)).retrieveByNickname(nickname);
    }

    @Test
    @DisplayName("비밀번호 업데이트")
    void testUpdatePassword() throws Exception {
        String newPassword = "newPassword";
        PasswordUpdateRequest request = new PasswordUpdateRequest(newPassword);
        String content = objectMapper.writeValueAsString(request);

        given(memberProfileService.updatePassword(newPassword, userDetails)).willReturn(emailResponse);

        mockMvc.perform(patch("/v1/members/profile/password")
                   .with(user(userDetails))
                   .content(content)
                   .contentType(APPLICATION_JSON)
                   .characterEncoding(UTF_8)
                   .with(csrf()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code", is(OK.value())))
               .andExpect(jsonPath("$.data.email", is(emailResponse.email())));
    }

    @Test
    @DisplayName("닉네임 업데이트")
    void testUpdateNickname() throws Exception {

        given(memberProfileService.updateNickname(memberResponse.nickname(), userDetails)).willReturn(memberResponse);

        mockMvc.perform(patch("/v1/members/profile/nickname?value={nickname}", memberResponse.nickname())
                   .with(user(userDetails))
                   .contentType(APPLICATION_JSON)
                   .characterEncoding(UTF_8)
                   .with(csrf()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code", is(OK.value())))
               .andExpect(jsonPath("$.data.nickname", is(memberResponse.nickname())));
    }

    @Test
    @DisplayName("프로필 업데이트")
    void testUpdateProfileImage() throws Exception {
        String path = System.getProperty("user.dir") + "/src/test/resources/images/test.jpeg";
        MockMultipartFile file = new MockMultipartFile("image", "test.jpeg", "jpeg", new FileInputStream(path));

        given(memberProfileService.updateProfileImage(file, userDetails)).willReturn(memberResponse);

        mockMvc.perform(MultipartBuilder.build("/v1/members/profile/image", "PATCH")
                                        .file(file)
                                        .characterEncoding(UTF_8)
                                        .with(user(userDetails))
                                        .with(csrf()))
               .andExpect(status().isOk());

    }

}
