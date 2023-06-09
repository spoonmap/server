package xyz.spoonmap.server.spoon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.spoonmap.server.dto.response.Response;
import xyz.spoonmap.server.spoon.dto.SpoonDeleteResponseDto;
import xyz.spoonmap.server.spoon.dto.SpoonResponseDto;
import xyz.spoonmap.server.spoon.service.SpoonService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/posts/{postId}/spoons")
public class SpoonControllerV1 {
    private final SpoonService spoonService;

    @GetMapping
    public ResponseEntity<Response<Slice<SpoonResponseDto>>> findAll(@AuthenticationPrincipal UserDetails userDetails,
                                                                     @PathVariable Long postId,
                                                                     @PageableDefault Pageable pageable) {
        Slice<SpoonResponseDto> response = spoonService.findAll(userDetails, postId, pageable);
        return ResponseEntity.status(OK)
                             .body(Response.of(OK.value(), response));
    }

    @GetMapping("/counts")
    public ResponseEntity<Response<Long>> count(@AuthenticationPrincipal UserDetails userDetails,
                                                @PathVariable Long postId) {
        Long count = spoonService.count(userDetails, postId);
        return ResponseEntity.status(OK)
                             .body(Response.of(OK.value(), count));
    }

    @PostMapping
    public ResponseEntity<Response<SpoonResponseDto>> addSpoon(@AuthenticationPrincipal UserDetails userDetails,
                                                               @PathVariable Long postId) {
        SpoonResponseDto responseDto = spoonService.add(userDetails, postId);
        return ResponseEntity.status(CREATED)
                             .body(Response.of(CREATED.value(), responseDto));
    }

    @DeleteMapping
    public ResponseEntity<Response<SpoonDeleteResponseDto>> deleteSpoon(@AuthenticationPrincipal UserDetails userDetails,
                                                                        @PathVariable Long postId) {
        SpoonDeleteResponseDto responseDto = spoonService.delete(userDetails, postId);
        return ResponseEntity.status(OK)
                             .body(Response.of(OK.value(), responseDto));
    }
}
