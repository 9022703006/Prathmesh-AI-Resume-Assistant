package com.example.aichatbot.controller;

import com.example.aichatbot.entity.Admin_register;
import com.example.aichatbot.service.Admin_Reg_service;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Profile("!gemini-only")
public class Admin_reg_Controller {

    private final Admin_Reg_service adminRegService;

    public Admin_reg_Controller(Admin_Reg_service adminRegService) {
        this.adminRegService = adminRegService;
    }

    // Admin Registration
    @PostMapping("/register")
    public ResponseEntity<Admin_register> registerAdmin(
            @RequestBody Admin_register admin) {

        Admin_register registeredAdmin =
                adminRegService.registerAdmin(admin);

        return ResponseEntity.ok(registeredAdmin);
    }

    // Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(
            @RequestParam String email,
            @RequestParam String otp) {

        String response =
                adminRegService.verifyOtp(email, otp);

        return ResponseEntity.ok(response);
    }
}
