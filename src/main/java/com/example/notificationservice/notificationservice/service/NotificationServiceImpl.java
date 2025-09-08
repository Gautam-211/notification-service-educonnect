package com.example.notificationservice.notificationservice.service;

import com.example.notificationservice.notificationservice.dto.CreateNotificationRequest;
import com.example.notificationservice.notificationservice.dto.NotificationResponse;
import com.example.notificationservice.notificationservice.entity.Notification;
import com.example.notificationservice.notificationservice.entity.NotificationStatus;
import com.example.notificationservice.notificationservice.exception.ResourceNotFoundException;
import com.example.notificationservice.notificationservice.repository.NotificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    @Override
    public NotificationResponse sendNotification(CreateNotificationRequest request) {
        log.info("Sending notification to: {}", request.getRecipientEmail());

        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .recipientEmail(request.getRecipientEmail())
                .subject(request.getSubject())
                .message(request.getMessage())
                .type(request.getType())
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        notification = notificationRepository.save(notification);

        try {
            sendEmail(notification);
            notification.setStatus(NotificationStatus.SENT);
            log.info("Email sent to {}", notification.getRecipientEmail());
        } catch (Exception e) {
            log.info("Failed to send email to {}: {}", notification.getRecipientEmail(), e.getMessage());
            notification.setStatus(NotificationStatus.FAILED);
            return mapToResponse(notification);
        }

        notificationRepository.save(notification);
        log.info("Notification status updated to SENT for id: {}", notification.getId());
        return mapToResponse(notification);
    }

    private void sendEmail(Notification notification) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setTo(notification.getRecipientEmail());
        helper.setSubject(notification.getSubject());
        helper.setText(notification.getMessage(), false);
        mailSender.send(mimeMessage);
    }

    @Override
    public NotificationResponse getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification with id : " + id + " not found"));
        return mapToResponse(notification);
    }

    @Override
    public List<NotificationResponse> getNotificationsByUserId(String userId) {
        return notificationRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .subject(notification.getSubject())
                .message(notification.getMessage())
                .status(notification.getStatus())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
