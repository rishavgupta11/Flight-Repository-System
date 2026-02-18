package com.flightapp.Controller;

import com.flightapp.model.Booking;
import com.flightapp.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/user/{userId}")
    public List<Booking> getBookingsByUserId(@PathVariable Long userId) {
        return bookingService.getBookingsByUserId(userId);
    }

    @PostMapping
    public Booking bookFlight(
            @RequestParam Long userId,
            @RequestParam Long flightId) {
        return bookingService.bookFlight(userId, flightId);
    }

    @PutMapping("{id}/cancel")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok("Booking cancelled successfully");
    }
}
