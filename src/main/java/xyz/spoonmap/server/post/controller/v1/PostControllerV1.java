package xyz.spoonmap.server.post.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.spoonmap.server.dto.response.Response;
import xyz.spoonmap.server.post.dto.request.PostSaveRequestDto;
import xyz.spoonmap.server.post.dto.request.PostUpdateRequestDto;
import xyz.spoonmap.server.post.dto.response.PostResponseDto;
import xyz.spoonmap.server.post.service.PostService;

import javax.validation.Valid;
import java.util.List;

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
    ResponseEntity<Response<PostResponseDto>> createPost(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart(value = "dto") @Valid PostSaveRequestDto postSaveRequestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        PostResponseDto responseDto = this.postService.createPost(userDetails, postSaveRequestDto, files);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(Response.of(HttpStatus.CREATED.value(), responseDto));
    }

    @PutMapping("/{postId}")
    ResponseEntity<Response<PostResponseDto>> updatePost(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("postId") Long id,
            @RequestPart(value = "dto") @Valid PostUpdateRequestDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        PostResponseDto responseDto = this.postService.updatePost(userDetails, id, requestDto, files);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(Response.of(HttpStatus.OK.value(), responseDto));
    }

    @DeleteMapping("/{postId}")
    ResponseEntity<Response<Long>> deletePost(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("postId") Long id) {
        Long postId = this.postService.deletePost(userDetails, id);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(Response.of(HttpStatus.OK.value(), postId));
    }
}
