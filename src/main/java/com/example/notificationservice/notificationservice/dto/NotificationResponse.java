package com.example.notificationservice.notificationservice.dto;

import com.example.notificationservice.notificationservice.entity.NotificationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private Long id;
    private String userId;
    private String subject;
    private String message;
    private NotificationStatus status;
    private LocalDateTime createdAt;
}
