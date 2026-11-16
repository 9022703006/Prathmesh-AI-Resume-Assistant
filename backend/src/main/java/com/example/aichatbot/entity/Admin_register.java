package com.example.aichatbot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.protobuf.Message;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "admin")
public class Admin_register{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "Admin_name",nullable = false)
    @NotBlank(message = "Username is required")
    private String username;
    @Column(nullable = false)
    @Email
    @NotBlank( message = "Email is required")
    private String email;
    private String password;
    @Column(nullable = false)
    private String conform_password;

    @Column(length = 6)
    @ToString.Exclude
    @JsonIgnore
    private String otp;

    @JsonIgnore
    @ToString.Exclude
    private String optexpired;

    @Column(nullable = false)
    private LocalDateTime CreatedAt;
    @Column(nullable = false)
    private LocalDateTime UpdateAt;

    @PrePersist
    protected void crearedAt(){
        CreatedAt =  LocalDateTime.now();
        UpdateAt = LocalDateTime.now();
    }
    @PreUpdate
    protected  void updateAt(){
        UpdateAt = LocalDateTime.now();
    }
}
