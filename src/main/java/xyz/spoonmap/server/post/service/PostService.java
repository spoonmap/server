package xyz.spoonmap.server.post.service;

import java.util.List;
import xyz.spoonmap.server.post.dto.request.PostRequestDto;
import xyz.spoonmap.server.post.dto.response.PostResponseDto;

public interface PostService {

    List<PostResponseDto> getAllPosts();

    PostResponseDto getPost(Long id);

    PostResponseDto createPost(PostRequestDto postRequestDto, Long memberId);

    PostResponseDto updatePost(Long id, PostRequestDto postRequestDto);

    void deletePost(Long id);
}
