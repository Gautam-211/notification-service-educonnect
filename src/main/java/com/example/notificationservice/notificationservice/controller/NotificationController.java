package com.example.notificationservice.notificationservice.controller;

import com.example.notificationservice.notificationservice.dto.CreateNotificationRequest;
import com.example.notificationservice.notificationservice.dto.NotificationResponse;
import com.example.notificationservice.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    // Send a new notification
    @PostMapping
    public ResponseEntity<NotificationResponse> sendNotification(
            @Valid @RequestBody CreateNotificationRequest request) {
        NotificationResponse response = notificationService.sendNotification(request);
        return ResponseEntity.ok(response);
    }

    // Get a notification by ID
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable Long id) {
        NotificationResponse response = notificationService.getNotificationById(id);
        return ResponseEntity.ok(response);
    }

    // Get all notifications for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByUserId(@PathVariable String userId) {
        List<NotificationResponse> responses = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(responses);
    }
}
