package com.example.aichatbot.repository;

import com.example.aichatbot.entity.Admin_register;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Admin_reg_Repository extends JpaRepository<Admin_register,Long> {
    Optional<Admin_register> findByEmail(String email);
    boolean existsByEmail(String email);
}
