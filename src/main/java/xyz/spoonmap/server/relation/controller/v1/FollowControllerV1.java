package xyz.spoonmap.server.relation.controller.v1;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.spoonmap.server.dto.response.Response;
import xyz.spoonmap.server.relation.dto.request.FollowRequest;
import xyz.spoonmap.server.relation.dto.response.FollowAddResponse;
import xyz.spoonmap.server.relation.dto.response.FollowResponse;
import xyz.spoonmap.server.relation.service.RelationService;

@RestController
@RequestMapping("/v1/members/follows")
@RequiredArgsConstructor
public class FollowControllerV1 {

    private final RelationService relationService;

    @PostMapping
    public ResponseEntity<Response<FollowAddResponse>> addFollow(@AuthenticationPrincipal UserDetails userDetails,
                                                                 @RequestBody FollowRequest followRequest
    ) {
        FollowAddResponse followAddResponse =
            relationService.requestFollow(userDetails, followRequest.targetMemberId());

        return ResponseEntity.status(CREATED)
                             .body(Response.of(CREATED.value(), followAddResponse));
    }

    @GetMapping
    public ResponseEntity<Response<FollowResponse>> retrieveFollow(@AuthenticationPrincipal UserDetails userDetails) {
        FollowResponse followResponse = relationService.retrieveFollows(userDetails);
        return ResponseEntity.status(OK)
                             .body(Response.of(OK.value(), followResponse));
    }

    @PatchMapping
    public ResponseEntity<Response<FollowAddResponse>> acceptFollow(@RequestBody FollowRequest followRequest,
                                                                    @AuthenticationPrincipal UserDetails userDetails) {
        FollowAddResponse followAddResponse = relationService.acceptFollow(followRequest.targetMemberId(), userDetails);

        return ResponseEntity.status(OK)
                             .body(Response.of(OK.value(), followAddResponse));
    }

    @GetMapping("/request")
    public ResponseEntity<Response<FollowResponse>> retrieveFollowRequest(
        @AuthenticationPrincipal UserDetails userDetails) {

        FollowResponse followResponse = relationService.retrieveFollowRequest(userDetails);

        return ResponseEntity.status(OK)
                             .body(Response.of(OK.value(), followResponse));
    }

    @PatchMapping("/rejection/{senderId}")
    public ResponseEntity<Response<FollowAddResponse>> rejectFollow(@PathVariable Long senderId,
                                                                    @AuthenticationPrincipal UserDetails userDetails) {
        FollowAddResponse followAddResponse = relationService.rejectFollow(senderId, userDetails);

        return ResponseEntity.status(OK)
                             .body(Response.of(OK.value(), followAddResponse));
    }

}
