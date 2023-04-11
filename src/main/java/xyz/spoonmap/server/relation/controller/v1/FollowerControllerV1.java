package xyz.spoonmap.server.relation.controller.v1;

import static org.springframework.http.HttpStatus.OK;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.spoonmap.server.dto.response.Response;
import xyz.spoonmap.server.relation.dto.response.FollowerResponse;
import xyz.spoonmap.server.relation.service.RelationService;

@RestController
@RequestMapping("/v1/members/followers")
@RequiredArgsConstructor
public class FollowerControllerV1 {

    private final RelationService relationService;

    @GetMapping
    public ResponseEntity<Response<FollowerResponse>> retrieveFollow(UserDetails userDetails) {
        FollowerResponse followerResponse = relationService.retrieveFollowers(userDetails);
        return ResponseEntity.status(OK)
                             .body(Response.of(OK.value(), followerResponse));
    }

    @GetMapping("/request")
    public ResponseEntity<Response<FollowerResponse>> retrieveFollowerRequest(UserDetails userDetails) {
        FollowerResponse followerResponse = relationService.retrieveFollowerRequest(userDetails);
        return ResponseEntity.status(OK)
                             .body(Response.of(OK.value(), followerResponse));
    }

}
