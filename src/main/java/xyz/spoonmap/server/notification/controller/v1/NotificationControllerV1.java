package xyz.spoonmap.server.notification.controller.v1;

import static org.springframework.http.HttpStatus.OK;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.spoonmap.server.dto.response.Response;
import xyz.spoonmap.server.notification.dto.response.NotificationResponse;
import xyz.spoonmap.server.notification.dto.response.SliceResponse;
import xyz.spoonmap.server.notification.service.NotificationService;

@RequestMapping("/v1/notifications")
@RestController
@RequiredArgsConstructor
public class NotificationControllerV1 {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<Response<SliceResponse<NotificationResponse>>> retrieveNotifications(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam(required = false) Long last,
        @RequestParam(defaultValue = "10") Integer size) {

        SliceResponse<NotificationResponse> notificationResponse =
            notificationService.retrieveNotification(userDetails, last, size);

        return Response.success(OK, OK.value(), notificationResponse);
    }

}
