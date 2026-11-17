package com.example.aichatbot.service;

import com.example.aichatbot.Enum.BookingStatus;
import com.example.aichatbot.entity.Booking;
import com.example.aichatbot.repository.BookingRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Profile("!gemini-only")
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking addBooking(Booking booking){
        return bookingRepository.save(booking);
    }

    public List<Booking> findstatus (BookingStatus status){
        return bookingRepository.findByStatus(status);

    }
}
