package xyz.spoonmap.server.post.service;

import org.springframework.web.multipart.MultipartFile;
import xyz.spoonmap.server.post.dto.request.PostSaveRequestDto;
import xyz.spoonmap.server.post.dto.request.PostUpdateRequestDto;
import xyz.spoonmap.server.post.dto.response.PostResponseDto;

import java.util.List;

public interface PostService {

    List<PostResponseDto> getAllPosts();

    PostResponseDto getPost(Long id);

    PostResponseDto createPost(Long memberId, PostSaveRequestDto requestDto, List<MultipartFile> files);

    PostResponseDto updatePost(Long memberId, Long id, PostUpdateRequestDto requestDto, List<MultipartFile> files);

    PostResponseDto deletePost(Long memberId, Long id);
}
