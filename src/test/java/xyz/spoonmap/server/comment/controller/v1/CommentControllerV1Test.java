package xyz.spoonmap.server.comment.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import xyz.spoonmap.server.authentication.CustomUserDetail;
import xyz.spoonmap.server.comment.dto.request.CommentSaveRequestDto;
import xyz.spoonmap.server.comment.dto.response.CommentResponseDto;
import xyz.spoonmap.server.comment.entity.Comment;
import xyz.spoonmap.server.comment.service.v1.CommentServiceV1;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.post.entity.Post;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentControllerV1.class)
@MockBean(JpaMetamodelMappingContext.class)
class CommentControllerV1Test {

    private static final Long postId = 42L;
    private static final Long memberId = 1L;
    private static final Member member = new Member();
    private static CustomUserDetail customUserDetail;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private CommentServiceV1 commentServiceV1;

    @BeforeAll()
    static void setUp() {
        ReflectionTestUtils.setField(member, "id", memberId);
        customUserDetail = new CustomUserDetail(member);
    }

    @Test
    void 게시물의_모든_댓글_조회() throws Exception {
        String url = "/v1/posts/" + postId + "/comments";


        Comment comment1 = Comment.builder()
                                  .content("comment1")
                                  .member(member)
                                  .build();
        ReflectionTestUtils.setField(comment1, "id", 1L);
        CommentResponseDto response1 = new CommentResponseDto(comment1);

        Comment comment2 = Comment.builder()
                                  .content("comment2")
                                  .member(member)
                                  .build();
        ReflectionTestUtils.setField(comment2, "id", 2L);
        CommentResponseDto response2 = new CommentResponseDto(comment2);

        List<CommentResponseDto> responses = List.of(response1, response2);

        given(commentServiceV1.findAllBy(postId)).willReturn(responses);

        mockMvc.perform(get(url).with(user(customUserDetail)))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("comment1")))
               .andExpect(content().string(containsString("comment2")));
    }

    @Test
    void create() throws Exception {
        String url = "/v1/posts/" + postId + "/comments";
        String content = "create content";

        CommentSaveRequestDto requestDto = new CommentSaveRequestDto(postId, null, content);
        String requestBody = objectMapper.writeValueAsString(requestDto);

        Comment comment = Comment.builder()
                                 .member(member)
                                 .parentComment(null)
                                 .content(content)
                                 .post(new Post())
                                 .build();

        CommentResponseDto response = new CommentResponseDto(comment);

        given(commentServiceV1.create(customUserDetail, postId, requestDto)).willReturn(response);

        mockMvc.perform(post(url).with(user(customUserDetail))
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(requestBody)
                                 .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(content().string(containsString(content)));
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}
