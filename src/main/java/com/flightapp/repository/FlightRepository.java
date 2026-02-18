package com.flightapp.repository;

import com.flightapp.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    // spring auto generates the sql for this method based on its name!
    List<Flight> findByOriginAndDestination(String origin, String destination);

    List<Flight> findByAvailableSeatsGreaterThan(int seats);
}
