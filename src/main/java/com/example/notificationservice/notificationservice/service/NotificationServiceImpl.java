package com.example.notificationservice.notificationservice.service;

import com.example.notificationservice.notificationservice.dto.CreateNotificationRequest;
import com.example.notificationservice.notificationservice.dto.NotificationResponse;
import com.example.notificationservice.notificationservice.entity.Notification;
import com.example.notificationservice.notificationservice.entity.NotificationStatus;
import com.example.notificationservice.notificationservice.repository.NotificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    @Override
    public NotificationResponse sendNotification(CreateNotificationRequest request) {
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
        } catch (Exception e) {
            notification.setStatus(NotificationStatus.FAILED);
            return mapToResponse(notification);
        }

        notificationRepository.save(notification);
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
                .orElseThrow(() -> new RuntimeException("Notification not found"));
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
