package com.example.aichatbot.service;

import com.example.aichatbot.entity.Admin_register;
import com.example.aichatbot.repository.Admin_reg_Repository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminRegServiceOtpTest {

    @Mock
    private Admin_reg_Repository adminRegRepository;

    @Mock
    private EmailService emailService;

    private Admin_Reg_service adminRegService;

    @BeforeEach
    void setUp() {
        adminRegService = new Admin_Reg_service(adminRegRepository, emailService);
    }

    @Test
    void testRegisterAdmin_Generates6DigitOtp_Sets5MinExpiry_AndDispatchesEmail() {
        Admin_register admin = new Admin_register();
        admin.setUsername("testadmin");
        admin.setEmail("admin@example.com");
        admin.setPassword("securePass123");
        admin.setConform_password("securePass123");

        when(adminRegRepository.existsByEmail("admin@example.com")).thenReturn(false);
        when(adminRegRepository.save(any(Admin_register.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Admin_register result = adminRegService.registerAdmin(admin);

        assertNotNull(result);

        // 1. Verify OTP generation is exactly 6 digits
        assertNotNull(result.getOtp());
        assertEquals(6, result.getOtp().length());
        assertTrue(result.getOtp().matches("\\d{6}"), "OTP must consist of 6 numeric digits");

        // 2. Verify OTP expiry is set to ~5 minutes in future
        assertNotNull(result.getOptexpired());
        LocalDateTime expiry = LocalDateTime.parse(result.getOptexpired());
        assertTrue(expiry.isAfter(LocalDateTime.now().plusMinutes(4)), "Expiry should be approximately 5 minutes in future");
        assertTrue(expiry.isBefore(LocalDateTime.now().plusMinutes(6)));

        // 3. Verify EmailService is invoked with correct recipient email and the exact same OTP
        verify(emailService, times(1)).sendOtpEmail(eq("admin@example.com"), eq(result.getOtp()));
    }

    @Test
    void testVerifyOtp_Success_ClearsOtpAndExpiry() {
        Admin_register admin = new Admin_register();
        admin.setEmail("admin@example.com");
        admin.setOtp("123456");
        admin.setOptexpired(LocalDateTime.now().plusMinutes(5).toString());

        when(adminRegRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(admin));
        when(adminRegRepository.save(any(Admin_register.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String response = adminRegService.verifyOtp("admin@example.com", "123456");

        assertEquals("OTP verified successfully", response);
        assertNull(admin.getOtp(), "OTP must be cleared after verification");
        assertNull(admin.getOptexpired(), "OTP expiry must be cleared after verification");
        verify(adminRegRepository, times(1)).save(admin);
    }

    @Test
    void testVerifyOtp_IncorrectOtp_RejectsVerification() {
        Admin_register admin = new Admin_register();
        admin.setEmail("admin@example.com");
        admin.setOtp("123456");
        admin.setOptexpired(LocalDateTime.now().plusMinutes(5).toString());

        when(adminRegRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(admin));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                adminRegService.verifyOtp("admin@example.com", "999999")
        );

        assertEquals("Invalid OTP", exception.getMessage());
        verify(adminRegRepository, never()).save(any(Admin_register.class));
    }

    @Test
    void testVerifyOtp_ExpiredOtp_RejectsVerification() {
        Admin_register admin = new Admin_register();
        admin.setEmail("admin@example.com");
        admin.setOtp("123456");
        admin.setOptexpired(LocalDateTime.now().minusMinutes(1).toString());

        when(adminRegRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(admin));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                adminRegService.verifyOtp("admin@example.com", "123456")
        );

        assertEquals("OTP expired", exception.getMessage());
        verify(adminRegRepository, never()).save(any(Admin_register.class));
    }

    @Test
    void testVerifyOtp_AdminNotFound_RejectsVerification() {
        when(adminRegRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                adminRegService.verifyOtp("unknown@example.com", "123456")
        );

        assertEquals("Admin not found", exception.getMessage());
    }

    @Test
    void testRegisterAdmin_EmailSendingFails_DeletesRecordAndThrowsException() {
        Admin_register admin = new Admin_register();
        admin.setUsername("testadmin");
        admin.setEmail("fail@example.com");
        admin.setPassword("password123");
        admin.setConform_password("password123");

        when(adminRegRepository.existsByEmail("fail@example.com")).thenReturn(false);
        when(adminRegRepository.save(any(Admin_register.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doThrow(new RuntimeException("SMTP connection failed"))
                .when(emailService).sendOtpEmail(eq("fail@example.com"), anyString());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                adminRegService.registerAdmin(admin)
        );

        assertTrue(exception.getMessage().contains("Failed to send OTP email"));
        verify(adminRegRepository, times(1)).delete(any(Admin_register.class));
    }

    @Test
    void testOtpExcludedFromJsonResponse() throws Exception {
        Admin_register admin = new Admin_register();
        admin.setUsername("testadmin");
        admin.setEmail("admin@example.com");
        admin.setOtp("654321");
        admin.setOptexpired(LocalDateTime.now().plusMinutes(5).toString());

        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        String json = mapper.writeValueAsString(admin);

        assertFalse(json.contains("654321"), "OTP must not be exposed in JSON output");
        assertFalse(json.contains("\"otp\""), "JSON representation must not contain otp field");
        assertFalse(json.contains("\"optexpired\""), "JSON representation must not contain optexpired field");
    }
}
