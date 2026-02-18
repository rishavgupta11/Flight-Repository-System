package com.flightapp.Controller;

import com.flightapp.model.Flight;
import com.flightapp.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    //GET  /api/flights  -> Get all flights
    @GetMapping
    public List<Flight> getAllFlights() {
        return flightService.getAllFlights();
    }

    //GET  /api/flights/{id}  -> Get single flight
    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlightById(id));
    }

    //GET  /api/flights/search?origin=DEL&destination=MUM
    @GetMapping("/search")
    public List<Flight> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination) {
        return flightService.searchFlights(origin, destination);
    }

    //POST  /api/flights  -> Add new flight
    @PostMapping
    public Flight addFlight(@RequestBody Flight flight) {
        return flightService.addFlight(flight);
    }

    //DELETE  /api/flights/{id}  -> Delete a flight
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.ok("Flight deleted successfully");
    }
}
