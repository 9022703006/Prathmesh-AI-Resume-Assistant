package com.example.aichatbot.controller;

import com.example.aichatbot.Enum.BookingStatus;
import com.example.aichatbot.entity.Booking;
import com.example.aichatbot.service.BookingService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/booking/vehicle")
@Profile("!gemini-only")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping()
    public Booking addbooking(@RequestBody Booking booking){
        return bookingService.addBooking(booking);
    }

    @GetMapping("/{status}")
    public List<Booking> getallstatus(@PathVariable BookingStatus bookingStatus){
        return bookingService.findstatus(bookingStatus);
    }

}
