package com.ridelink.ridemanagement.service;

import com.ridelink.ridemanagement.model.Ride;
import com.ridelink.ridemanagement.model.RideStatus;
import com.ridelink.ridemanagement.repository.RideRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideService {

    private final RideRepository rideRepository;

    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    public Ride createRide(Ride ride) {
        return rideRepository.save(ride);
    }

    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    public Ride getRideById(String rideId) {
    return rideRepository.findById(rideId).orElse(null);
    }

    public Ride updateRideStatus(String rideId, RideStatus status) {
    Ride ride = rideRepository.findById(rideId).orElse(null);

    if (ride != null) {
        ride.setStatus(status);
        return rideRepository.save(ride);
    }

    return null;
    }
}