package com.example.aichatbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String fromEmail;

    public EmailService(JavaMailSender mailSender,
                        @Value("${spring.mail.username:}") String fromEmail) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
    }

    /**
     * Sends an OTP email to the specified recipient.
     *
     * @param toEmail recipient email address
     * @param otp     the 6-digit OTP code to send
     */
    public void sendOtpEmail(String toEmail, String otp) {
        if (toEmail == null || toEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipient email cannot be null or empty");
        }
        if (otp == null || otp.trim().isEmpty()) {
            throw new IllegalArgumentException("OTP cannot be null or empty");
        }

        logger.info("Preparing to send OTP email to recipient: {}", toEmail);

        SimpleMailMessage message = new SimpleMailMessage();
        if (fromEmail != null && !fromEmail.trim().isEmpty()) {
            message.setFrom(fromEmail);
        }
        message.setTo(toEmail.trim());
        message.setSubject("Your Verification OTP");
        message.setText("Hello,\n\n"
                + "Your One-Time Password (OTP) for verification is: " + otp.trim() + "\n\n"
                + "This OTP is valid for 5 minutes. Please do not share this code with anyone.\n\n"
                + "Thank you,\n"
                + "AI Chatbot Team");

        mailSender.send(message);
        logger.info("OTP email successfully dispatched to recipient: {}", toEmail);
    }
}
