package xyz.spoonmap.server.post.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import xyz.spoonmap.server.authentication.CustomUserDetail;
import xyz.spoonmap.server.member.dto.response.MemberResponse;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.post.dto.request.PostSaveRequestDto;
import xyz.spoonmap.server.post.dto.request.PostUpdateRequestDto;
import xyz.spoonmap.server.post.dto.response.PostResponseDto;
import xyz.spoonmap.server.post.entity.enums.MealTime;
import xyz.spoonmap.server.post.service.v1.PostServiceV1;
import xyz.spoonmap.server.restaurant.dto.request.RestaurantRequestDto;
import xyz.spoonmap.server.restaurant.dto.response.RestaurantResponseDto;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostControllerV1.class)
@MockBean(JpaMetamodelMappingContext.class)
class PostControllerV1Test {

    private static final String baseUrl = "/v1/posts/";
    private static final Long memberId = 12L;
    private static CustomUserDetail customUserDetail;
    private static Member member;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    WebApplicationContext context;
    @MockBean
    private PostServiceV1 postServiceV1;

    @BeforeAll
    static void init() {
        member = Member.builder()
                       .nickname("test")
                       .password("testpass")
                       .name("test")
                       .email("test@test.com")
                       .build();
        ReflectionTestUtils.setField(member, "id", memberId);
        customUserDetail = new CustomUserDetail(member);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                                 .apply(springSecurity())
                                 .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                                 .alwaysDo(print())
                                 .build();
    }

    @Test
    void 모든_게시물_조회() throws Exception {
        Long postId = 42L;

        String expectedTitle = "test title";
        MemberResponse memberResponse = new MemberResponse(member);

        PostResponseDto postResponseDto = PostResponseDto.builder()
                                                         .id(postId)
                                                         .author(memberResponse)
                                                         .title(expectedTitle)
                                                         .build();
        List<PostResponseDto> postResponseDtoList = List.of(postResponseDto);

        given(postServiceV1.getAllPosts()).willReturn(postResponseDtoList);

        mockMvc.perform(get(baseUrl).with(user(customUserDetail)))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString(postId.toString())))
               .andExpect(content().string(containsString(expectedTitle)));
        then(postServiceV1).should(times(1)).getAllPosts();
    }

    @Test
    void 게시물_아이디로_조회() throws Exception {
        Long postId = 42L;

        String expectedTitle = "test title";
        MemberResponse memberResponse = new MemberResponse(member);

        PostResponseDto postResponseDto = PostResponseDto.builder()
                                                         .id(postId)
                                                         .author(memberResponse)
                                                         .title(expectedTitle)
                                                         .build();

        given(postServiceV1.getPost(postId)).willReturn(postResponseDto);

        mockMvc.perform(get(baseUrl + postId).with(user(customUserDetail)))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString(postId.toString())))
               .andExpect(content().string(containsString(expectedTitle)));
        then(postServiceV1).should(times(1)).getPost(postId);
    }

    @Test
    void 게시물_생성() throws Exception {
        String fileName = "test";
        String contentType = "jpeg";
        String path = "src/test/resources/images/" + fileName + "." + contentType;

        MockMultipartFile file = new MockMultipartFile("files", fileName + "." + contentType, contentType, new FileInputStream(path));

        RestaurantRequestDto restaurant = new RestaurantRequestDto("name", "address", 1.2F, 3.4F);

        PostSaveRequestDto requestDto = new PostSaveRequestDto(restaurant, 1L, "title", "content", MealTime.아침, (byte) 10);

        String url = "test.com";
        List<String> photoUrls = List.of(url);

        MemberResponse memberResponse = new MemberResponse(member);

        RestaurantResponseDto restaurantResponse = new RestaurantResponseDto("name", "address", 1.2F, 2.4F);

        PostResponseDto responseDto = new PostResponseDto(1L, memberResponse, "title", photoUrls, restaurantResponse, "한식", (byte) 10, "content", MealTime.아침, LocalDateTime.now(), LocalDateTime.now());

        given(postServiceV1.createPost(customUserDetail, requestDto, List.of(file))).willReturn(responseDto);

        MockMultipartFile dtoToPart = new MockMultipartFile("dto", "dto", "application/json", objectMapper.writeValueAsString(requestDto)
                                                                                                          .getBytes(StandardCharsets.UTF_8));
        mockMvc.perform(multipart(baseUrl)
                       .file(file)
                       .file(dtoToPart)
                       .with(user(customUserDetail))
                       .with(csrf())
                       .accept(MediaType.APPLICATION_JSON)
                       .characterEncoding(StandardCharsets.UTF_8))
               .andExpect(status().isCreated());
        then(postServiceV1).should(times(1)).createPost(customUserDetail, requestDto, List.of(file));
    }

    @Test
    void 게시물_수정() throws Exception {
        Long postId = 42L;
        String fileName = "test";
        String contentType = "jpeg";
        String path = "src/test/resources/images/" + fileName + "." + contentType;

        MockMultipartFile file = new MockMultipartFile("files", fileName + "." + contentType, contentType, new FileInputStream(path));

        RestaurantRequestDto restaurant = new RestaurantRequestDto("name", "address", 1.2F, 3.4F);

        PostUpdateRequestDto requestDto = new PostUpdateRequestDto(restaurant, 1L, "title", "content", MealTime.아침, (byte) 10);

        String url = "test.com";
        List<String> photoUrls = List.of(url);

        MemberResponse memberResponse = new MemberResponse(member);

        RestaurantResponseDto restaurantResponse = new RestaurantResponseDto("name", "address", 1.2F, 2.4F);

        PostResponseDto responseDto = new PostResponseDto(1L, memberResponse, "title", photoUrls, restaurantResponse, "한식", (byte) 10, "content", MealTime.아침, LocalDateTime.now(), LocalDateTime.now());

        given(postServiceV1.updatePost(customUserDetail, postId, requestDto, List.of(file))).willReturn(responseDto);

        MockMultipartFile dtoToPart = new MockMultipartFile("dto", "dto", "application/json", objectMapper.writeValueAsString(requestDto)
                                                                                                          .getBytes(StandardCharsets.UTF_8));

        MockMultipartHttpServletRequestBuilder builder = multipart(baseUrl + postId);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        mockMvc.perform(builder
                       .file(file)
                       .file(dtoToPart)
                       .with(user(customUserDetail))
                       .with(csrf())
                       .accept(MediaType.APPLICATION_JSON)
                       .characterEncoding(StandardCharsets.UTF_8))
               .andExpect(status().isOk());
        then(postServiceV1).should(times(1)).updatePost(customUserDetail, postId, requestDto, List.of(file));
    }

    @Test
    void 게시물_삭제() throws Exception {
        Long postId = 42L;

        given(postServiceV1.deletePost(customUserDetail, postId)).willReturn(postId);

        mockMvc.perform(delete(baseUrl + postId).with(user(customUserDetail))
                                                .with(csrf()))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString(postId.toString())));
        then(postServiceV1).should(times(1)).deletePost(customUserDetail, postId);
    }
}
