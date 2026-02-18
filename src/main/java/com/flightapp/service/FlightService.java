package com.flightapp.service;

import com.flightapp.model.Flight;
import com.flightapp.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService {

    @Autowired // spring automatically injects FlightRepos here
    private FlightRepository flightRepository;

    // Get all flights
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    // Get single flight by ID
    public Flight getFlightById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("Flight not found with id: " + id));
    }

    // Search flights by origin and destination
    public List<Flight> searchFlights(String origin, String destination) {
        return flightRepository.findByOriginAndDestination(origin, destination);
    }

    // Add a new flight;
    public Flight addFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    // Delete a flight
    public void deleteFlight(Long id) {
        flightRepository.deleteById(id);
    }
}
