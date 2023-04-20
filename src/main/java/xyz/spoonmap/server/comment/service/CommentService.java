package xyz.spoonmap.server.comment.service;

import org.springframework.security.core.userdetails.UserDetails;
import xyz.spoonmap.server.comment.dto.request.CommentSaveRequestDto;
import xyz.spoonmap.server.comment.dto.request.CommentUpdateRequestDto;
import xyz.spoonmap.server.comment.dto.response.CommentResponseDto;

import java.util.List;

public interface CommentService {

    List<CommentResponseDto> findAllBy(Long postId);

    CommentResponseDto create(UserDetails userDetails, Long postId, CommentSaveRequestDto requestDto);

    CommentResponseDto update(UserDetails userDetails, Long commentId, CommentUpdateRequestDto requestDto);

    Long delete(UserDetails userDetails, Long commentId);

}
