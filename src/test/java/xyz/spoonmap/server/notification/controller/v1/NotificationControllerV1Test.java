package xyz.spoonmap.server.notification.controller.v1;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
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
import xyz.spoonmap.server.authentication.CustomUserDetail;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.notification.dto.response.NotificationResponse;
import xyz.spoonmap.server.notification.dto.response.SliceResponse;
import xyz.spoonmap.server.notification.service.NotificationService;
import xyz.spoonmap.server.util.RandomEntityGenerator;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(NotificationControllerV1.class)
class NotificationControllerV1Test {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    NotificationControllerV1 notificationControllerV1;

    @MockBean
    NotificationService notificationService;

    UserDetails userDetails;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                                 .apply(springSecurity())
                                 .defaultResponseCharacterEncoding(UTF_8)
                                 .alwaysDo(print())
                                 .build();

        Member member = RandomEntityGenerator.createWithId(Member.class);
        userDetails = new CustomUserDetail(member);
    }

    @Test
    @DisplayName("알림 조회")
    void testRetrieveNotifications() throws Exception {
        Long last = 1L;
        int size = 10;
        SliceResponse<NotificationResponse> response = new SliceResponse<>(false, List.of());

        given(notificationService.retrieveNotification(userDetails, last, size)).willReturn(response);

        mockMvc.perform(get("/v1/notifications?last={last}&size={size}", last, size)
                   .with(user(userDetails))
                   .characterEncoding(UTF_8))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code", is(OK.value())))
               .andExpect(jsonPath("$.data.hasNext", is(false)))
               .andExpect(jsonPath("$.data.content.size()", is(0)));
    }

}
