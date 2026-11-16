package com.example.aichatbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = new EmailService(mailSender, "noreply@aichatbot.com");
    }

    @Test
    void testSendOtpEmail_ConstructsCorrectMessageAndSends() {
        emailService.sendOtpEmail("user@example.com", "456789");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();
        assertNotNull(sentMessage);
        assertArrayEquals(new String[]{"user@example.com"}, sentMessage.getTo());
        assertEquals("noreply@aichatbot.com", sentMessage.getFrom());
        assertTrue(sentMessage.getSubject().contains("OTP"));
        assertNotNull(sentMessage.getText());
        assertTrue(sentMessage.getText().contains("456789"), "Email text must contain the OTP");
        assertTrue(sentMessage.getText().contains("5 minutes"), "Email text must state 5-minute expiry");
    }

    @Test
    void testSendOtpEmail_NullOrEmptyEmail_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                emailService.sendOtpEmail(null, "123456")
        );
        assertThrows(IllegalArgumentException.class, () ->
                emailService.sendOtpEmail("   ", "123456")
        );
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendOtpEmail_NullOrEmptyOtp_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                emailService.sendOtpEmail("user@example.com", null)
        );
        assertThrows(IllegalArgumentException.class, () ->
                emailService.sendOtpEmail("user@example.com", "  ")
        );
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }
}
