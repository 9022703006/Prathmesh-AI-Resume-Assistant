package com.example.aichatbot.repository;

import com.example.aichatbot.Enum.BookingStatus;
import com.example.aichatbot.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findByName(String name);
    List<Booking> findByStatus(BookingStatus status);
}
