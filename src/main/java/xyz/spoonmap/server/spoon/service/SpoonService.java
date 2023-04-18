package xyz.spoonmap.server.spoon.service;

import org.springframework.security.core.userdetails.UserDetails;
import xyz.spoonmap.server.spoon.dto.SpoonDeleteResponseDto;
import xyz.spoonmap.server.spoon.dto.SpoonResponseDto;
import xyz.spoonmap.server.spoon.entity.Spoon;

import java.util.List;

public interface SpoonService {
    List<SpoonResponseDto> findAll(UserDetails userDetails, Long postId);

    Long count(UserDetails userDetails, Long postId);

    SpoonResponseDto add(UserDetails userDetails, Long postId);

    SpoonDeleteResponseDto delete(UserDetails userDetails, Long postId);
}
