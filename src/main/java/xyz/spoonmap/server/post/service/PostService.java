package xyz.spoonmap.server.post.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import xyz.spoonmap.server.post.dto.request.PostSaveRequestDto;
import xyz.spoonmap.server.post.dto.request.PostUpdateRequestDto;
import xyz.spoonmap.server.post.dto.response.PostResponseDto;

import java.util.List;

public interface PostService {

    List<PostResponseDto> getAllPosts();

    PostResponseDto getPost(Long id);

    PostResponseDto createPost(UserDetails userDetails, PostSaveRequestDto requestDto, List<MultipartFile> files);

    PostResponseDto updatePost(UserDetails userDetails, Long id, PostUpdateRequestDto requestDto, List<MultipartFile> files);

    Long deletePost(UserDetails userDetails, Long id);
}
