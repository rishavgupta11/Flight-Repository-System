package com.flightapp.service;

import com.flightapp.exception.BadRequestException;
import com.flightapp.model.Booking;
import com.flightapp.model.Flight;
import com.flightapp.model.User;
import com.flightapp.repository.BookingRepository;
import com.flightapp.repository.FlightRepository;
import com.flightapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void cancelBookingShouldRefundWalletRestoreSeatAndMarkCancelled() {
        User user = new User();
        user.setId(1L);
        user.setWalletBalance(3000.0);

        Flight flight = new Flight();
        flight.setId(10L);
        flight.setPrice(2000.0);
        flight.setAvailableSeats(9);

        Booking booking = new Booking();
        booking.setId(100L);
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setStatus("CONFIRMED");

        when(bookingRepository.findById(100L)).thenReturn(Optional.of(booking));

        bookingService.cancelBooking(100L);

        assertEquals(5000.0, user.getWalletBalance());
        assertEquals(10, flight.getAvailableSeats());
        assertEquals("CANCELLED", booking.getStatus());

        verify(userRepository).save(user);
        verify(flightRepository).save(flight);
        verify(bookingRepository).save(booking);
    }

    @Test
    void cancelBookingShouldRejectAlreadyCancelledBooking() {
        Booking booking = new Booking();
        booking.setId(101L);
        booking.setStatus("CANCELLED");

        when(bookingRepository.findById(101L)).thenReturn(Optional.of(booking));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> bookingService.cancelBooking(101L));

        assertEquals("Booking is already cancelled", exception.getMessage());
        verify(userRepository, never()).save(org.mockito.ArgumentMatchers.any(User.class));
        verify(flightRepository, never()).save(org.mockito.ArgumentMatchers.any(Flight.class));
        verify(bookingRepository, never()).save(org.mockito.ArgumentMatchers.any(Booking.class));
    }
}
