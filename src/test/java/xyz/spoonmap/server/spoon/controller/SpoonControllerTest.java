package xyz.spoonmap.server.spoon.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import xyz.spoonmap.server.authentication.CustomUserDetail;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.spoon.dto.SpoonDeleteResponseDto;
import xyz.spoonmap.server.spoon.dto.SpoonResponseDto;
import xyz.spoonmap.server.spoon.entity.Spoon;
import xyz.spoonmap.server.spoon.service.SpoonService;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SpoonControllerV1.class)
@MockBean(JpaMetamodelMappingContext.class)
class SpoonControllerTest {

    private static final Member member = new Member();
    private static final Long memberId = 42L;
    private static final Long postId = 12L;
    @Autowired
    WebApplicationContext context;
    MockMvc mockMvc;
    private UserDetails userDetails;
    private String expectCode = "$.code";
    @MockBean
    private SpoonService spoonService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                                 .apply(springSecurity())
                                 .defaultResponseCharacterEncoding(UTF_8)
                                 .alwaysDo(print())
                                 .build();

        ReflectionTestUtils.setField(member, "id", memberId);
        userDetails = new CustomUserDetail(member);
    }

    @Test
    void 게시물의_모든_스푼_조회() throws Exception {
        Spoon spoon = new Spoon();
        ReflectionTestUtils.setField(spoon, "id", new Spoon.Pk(memberId, postId));

        SpoonResponseDto responseDto = new SpoonResponseDto(spoon, member);
        Pageable pageable = PageRequest.of(0, 10);
        Slice<SpoonResponseDto> slice = new SliceImpl<>(List.of(responseDto));
        given(spoonService.findAll(userDetails, postId, pageable)).willReturn(slice);

        mockMvc.perform(get("/v1/posts/{postId}/spoons", postId).with(user(userDetails))
                                                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath(expectCode, equalTo(HttpStatus.OK.value())))
               .andExpect(jsonPath("$.data", notNullValue()))
               .andExpect(content().string(containsString(memberId.toString())))
               .andExpect(content().string(containsString(postId.toString())));
    }

    @Test
    void 게시물의_스푼_갯수_조회() throws Exception {
        Long expected = 24L;
        given(spoonService.count(userDetails, postId)).willReturn(expected);

        mockMvc.perform(get("/v1/posts/{postId}/spoons/counts", postId).with(user(userDetails))
                                                                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath(expectCode, equalTo(HttpStatus.OK.value())))
               .andExpect(jsonPath("$.data", notNullValue()))
               .andExpect(content().string(containsString(expected.toString())));
    }


    @Test
    void 게시물에_스푼_추가() throws Exception {
        Spoon spoon = new Spoon();
        ReflectionTestUtils.setField(spoon, "id", new Spoon.Pk(memberId, postId));
        SpoonResponseDto responseDto = new SpoonResponseDto(spoon, member);

        given(spoonService.add(userDetails, postId)).willReturn(responseDto);

        mockMvc.perform(post("/v1/posts/{postId}/spoons", postId).with(user(userDetails))
                                                                 .with(csrf())
                                                                 .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(jsonPath(expectCode, equalTo(HttpStatus.CREATED.value())))
               .andExpect(jsonPath("$.data", notNullValue()))
               .andExpect(content().string(containsString(memberId.toString())))
               .andExpect(content().string(containsString(postId.toString())));
    }

    @Test
    void 게시물_스푼_삭제() throws Exception {
        SpoonDeleteResponseDto responseDto = new SpoonDeleteResponseDto(new Spoon.Pk(memberId, postId));
        given(spoonService.delete(userDetails, postId)).willReturn(responseDto);

        mockMvc.perform(delete("/v1/posts/{postId}/spoons", postId).with(user(userDetails))
                                                                   .with(csrf())
                                                                   .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath(expectCode, equalTo(HttpStatus.OK.value())))
               .andExpect(jsonPath("$.data", notNullValue()))
               .andExpect(content().string(containsString(memberId.toString())))
               .andExpect(content().string(containsString(postId.toString())));
    }
}
