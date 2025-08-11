package com.example.notificationservice.notificationservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The ID of the user who will receive the notification
    @Column(nullable = false)
    private String userId;

    // The email address where the notification will be sent
    @Column(nullable = false)
    private String recipientEmail;

    // The subject of the notification email
    @Column(nullable = false)
    private String subject;

    // The type/category of the notification
    @Column(nullable = false)
    private String type; // e.g., COURSE_PUBLISHED, NEW_REVIEW, etc.

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void setTimestamp() {
        this.createdAt = LocalDateTime.now();
        this.status = NotificationStatus.UNREAD;
    }
}
