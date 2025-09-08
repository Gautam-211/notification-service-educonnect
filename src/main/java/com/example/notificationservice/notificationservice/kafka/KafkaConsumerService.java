package com.example.notificationservice.notificationservice.kafka;

import com.example.notificationservice.notificationservice.dto.CourseEvent;
import com.example.notificationservice.notificationservice.dto.CreateNotificationRequest;
import com.example.notificationservice.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final NotificationService notificationService;

    @KafkaListener(topics = "course-events", groupId = "notification-service", containerFactory = "kafkaListenerContainerFactory")
    public void consume(CourseEvent event) {
        log.info("Received event: {}", event);

        CreateNotificationRequest request = event.getPayload();

        // Trigger notification flow
        log.info("Triggering notification for event: {}", event.getEventType());
        notificationService.sendNotification(request);

        log.info("Notification process completed for event: {}", event.getEventType());
    }
}
