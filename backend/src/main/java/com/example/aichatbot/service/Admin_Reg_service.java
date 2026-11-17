package com.example.aichatbot.service;

import com.example.aichatbot.entity.Admin_register;
import com.example.aichatbot.repository.Admin_reg_Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@Profile("!gemini-only")
public class Admin_Reg_service {

    private static final Logger logger = LoggerFactory.getLogger(Admin_Reg_service.class);

    private final Admin_reg_Repository adminRegRepository;
    private final EmailService emailService;
    private final SecureRandom secureRandom = new SecureRandom();

    public Admin_Reg_service(Admin_reg_Repository adminRegRepository, EmailService emailService) {
        this.adminRegRepository = adminRegRepository;
        this.emailService = emailService;
    }

    public Admin_register registerAdmin(Admin_register admin) {

        // Check email already exists
        if (adminRegRepository.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Check password and confirm password
        if (!admin.getPassword().equals(admin.getConform_password())) {
            throw new RuntimeException(
                    "Password and confirm password do not match"
            );
        }

        String otp = String.valueOf(
                100000 + secureRandom.nextInt(900000)
        );

        String otpExpired = LocalDateTime.now()
                .plusMinutes(5)
                .toString();

        admin.setOtp(otp);
        admin.setOptexpired(otpExpired);

        Admin_register savedAdmin = adminRegRepository.save(admin);

        // Send the same OTP to owner's email via EmailService
        try {
            emailService.sendOtpEmail(savedAdmin.getEmail(), otp);
        } catch (Exception e) {
            logger.error("Failed to send OTP email to {}: {}", savedAdmin.getEmail(), e.getMessage());
            try {
                adminRegRepository.delete(savedAdmin);
            } catch (Exception delEx) {
                logger.error("Failed to delete unverified admin record after email failure: {}", delEx.getMessage());
            }
            throw new RuntimeException("Failed to send OTP email. Registration could not be completed: " + e.getMessage(), e);
        }

        return savedAdmin;
    }


    // Verify OTP
    public String verifyOtp(String email, String otp) {

        Admin_register admin = adminRegRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Admin not found")
                );

        // Check OTP
        if (admin.getOtp() == null || !admin.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        // Check OTP expiry
        if (admin.getOptexpired() == null) {
            throw new RuntimeException("OTP expired");
        }

        LocalDateTime expiryTime =
                LocalDateTime.parse(admin.getOptexpired());

        if (LocalDateTime.now().isAfter(expiryTime)) {
            throw new RuntimeException("OTP expired");
        }

        // OTP verified
        admin.setOtp(null);
        admin.setOptexpired(null);

        adminRegRepository.save(admin);

        return "OTP verified successfully";
    }
}