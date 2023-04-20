package xyz.spoonmap.server.spoon.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.userdetails.UserDetails;
import xyz.spoonmap.server.spoon.dto.SpoonDeleteResponseDto;
import xyz.spoonmap.server.spoon.dto.SpoonResponseDto;

public interface SpoonService {
    Slice<SpoonResponseDto> findAll(UserDetails userDetails, Long postId, Pageable pageable);

    Long count(UserDetails userDetails, Long postId);

    SpoonResponseDto add(UserDetails userDetails, Long postId);

    SpoonDeleteResponseDto delete(UserDetails userDetails, Long postId);
}
