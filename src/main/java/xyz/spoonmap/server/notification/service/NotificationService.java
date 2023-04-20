package xyz.spoonmap.server.notification.service;

import org.springframework.security.core.userdetails.UserDetails;
import xyz.spoonmap.server.notification.dto.response.NotificationResponse;
import xyz.spoonmap.server.notification.dto.response.SliceResponse;

public interface NotificationService {

    SliceResponse<NotificationResponse> retrieveNotification(UserDetails userDetails,
                                                             Long lastNotificationId,
                                                             int size);

}
