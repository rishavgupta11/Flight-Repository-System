package com.flightapp.service;

import com.flightapp.model.Booking;
import com.flightapp.model.Flight;
import com.flightapp.model.User;
import com.flightapp.repository.BookingRepository;
import com.flightapp.repository.FlightRepository;
import com.flightapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingService {

    @Autowired private BookingRepository bookingRepository;
    @Autowired private FlightRepository flightRepository;
    @Autowired private UserRepository userRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Transactional // if anything fails, whole operation rolls back
    public Booking bookFlight(Long userId, Long flightId) {

        // Get user
        User user = userRepository.findById(userId)
                .orElseThrow( () -> new RuntimeException("User not found"));

        // Get flight
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow( () -> new RuntimeException("Flight not found"));

        // check seat availability
        if(flight.getAvailableSeats() <= 0) {
            throw new RuntimeException("No seats available!");
        }

        // check wallet balance
        if(user.getWalletBalance() < flight.getPrice()) {
            throw new RuntimeException("Insufficient wallet balance!");
        }

        // deduct money from wallet
        user.setWalletBalance(user.getWalletBalance() - flight.getPrice());
        userRepository.save(user);

        // reduce available seats
         flight.setAvailableSeats(flight.getAvailableSeats() - 1);
         flightRepository.save(flight);

        // create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setStatus("CONFIRMED");

        return bookingRepository.save(booking);
    }

    public void cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
    }
}
