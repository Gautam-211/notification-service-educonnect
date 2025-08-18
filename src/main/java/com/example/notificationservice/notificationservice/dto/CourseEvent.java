package com.example.notificationservice.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseEvent {
    private String eventType;
    private CreateNotificationRequest payload;
}
