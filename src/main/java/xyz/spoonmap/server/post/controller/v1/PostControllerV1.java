package xyz.spoonmap.server.post.controller.v1;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.spoonmap.server.dto.response.Response;
import xyz.spoonmap.server.post.dto.request.PostRequestDto;
import xyz.spoonmap.server.post.dto.response.PostResponseDto;
import xyz.spoonmap.server.post.service.PostService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/posts")
public class PostControllerV1 {

    private final PostService postService;

    @GetMapping()
    ResponseEntity<Response<List<PostResponseDto>>> getAllPosts() {
        List<PostResponseDto> responseDtos = this.postService.getAllPosts();
        return ResponseEntity.status(HttpStatus.OK)
                             .body(Response.of(HttpStatus.OK.value(), responseDtos));
    }


    @GetMapping("/{postId}")
    ResponseEntity<Response<PostResponseDto>> getPost(@PathVariable("postId") Long id) {
        PostResponseDto responseDto = this.postService.getPost(id);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(Response.of(HttpStatus.OK.value(), responseDto));
    }

    @PostMapping()
    ResponseEntity<Response<PostResponseDto>> createPost(Long memberId, @RequestBody @Valid PostRequestDto postRequestDto) {
        PostResponseDto responseDto = this.postService.createPost(postRequestDto, memberId);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(Response.of(HttpStatus.CREATED.value(), responseDto));
    }

    @PutMapping("/{postId}")
    ResponseEntity<Response<PostResponseDto>> updatePost(@PathVariable("postId") Long id,
            @RequestBody @Valid PostRequestDto postRequestDto) {
        PostResponseDto responseDto = this.postService.updatePost(id, postRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(Response.of(HttpStatus.OK.value(), responseDto));
    }

    @DeleteMapping("/{postId}")
    ResponseEntity<Void> deletePost(@PathVariable("postId") Long id) {
        this.postService.deletePost(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
