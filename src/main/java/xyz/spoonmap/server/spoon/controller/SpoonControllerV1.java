package xyz.spoonmap.server.spoon.controller;

import lombok.RequiredArgsConstructor;
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
import xyz.spoonmap.server.spoon.dto.SpoonResponseDto;
import xyz.spoonmap.server.spoon.entity.Spoon;
import xyz.spoonmap.server.spoon.service.SpoonService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/posts/{postId}/spoons")
public class SpoonControllerV1 {
    private final SpoonService spoonService;

    @GetMapping
    public ResponseEntity<Response<List<SpoonResponseDto>>> findAll(@AuthenticationPrincipal UserDetails userDetails,
                                                                    @PathVariable Long postId) {
        List<SpoonResponseDto> responseDtos = spoonService.findAll(userDetails, postId);
        return ResponseEntity.status(OK)
                             .body(Response.of(OK.value(), responseDtos));
    }

    @PostMapping
    public ResponseEntity<Response<SpoonResponseDto>> addSpoon(@AuthenticationPrincipal UserDetails userDetails,
                                                               @PathVariable Long postId) {
        SpoonResponseDto responseDto = spoonService.add(userDetails, postId);
        return ResponseEntity.status(CREATED)
                             .body(Response.of(CREATED.value(), responseDto));
    }

    @DeleteMapping
    public ResponseEntity<Response<Spoon.Pk>> deleteSpoon(@AuthenticationPrincipal UserDetails userDetails,
                                                          @PathVariable Long postId) {
        Spoon.Pk id = spoonService.delete(userDetails, postId);
        return ResponseEntity.status(OK)
                             .body(Response.of(OK.value(), id));
    }
}
