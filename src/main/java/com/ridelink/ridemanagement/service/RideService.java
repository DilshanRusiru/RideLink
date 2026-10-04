package com.ridelink.ridemanagement.service;

import com.ridelink.ridemanagement.model.Ride;
import com.ridelink.ridemanagement.model.RideStatus;
import com.ridelink.ridemanagement.repository.RideRepository;
import com.ridelink.ridemanagement.dto.CreateRideRequest;
import org.springframework.stereotype.Service;
import com.ridelink.ridemanagement.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class RideService {

    private final RideRepository rideRepository;

    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
        
    }

    public Ride createRide(CreateRideRequest request) {

    Ride ride = new Ride();

    long nextId = rideRepository.count() + 1;
    ride.setRideId(String.format("RI%03d", nextId));

    ride.setPassengerId(request.getPassengerId());
    ride.setPickupLocation(request.getPickupLocation());
    ride.setDestination(request.getDestination());
    ride.setStatus(RideStatus.REQUESTED);

    return rideRepository.save(ride);
}

    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    public Ride getRideById(String rideId) {
    return rideRepository.findById(rideId)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Ride not found with ID: " + rideId
                    )
            );
}

    public Ride updateRideStatus(String rideId, RideStatus newStatus) {
    Ride ride = rideRepository.findById(rideId)
        .orElseThrow(() ->
                new ResourceNotFoundException(
                        "Ride not found with ID: " + rideId
                )
        );

    RideStatus currentStatus = ride.getStatus();

    if (!isValidTransition(currentStatus, newStatus)) {
        throw new IllegalStateException(
                "Invalid ride status transition from "
                        + currentStatus + " to " + newStatus
        );
    }

    ride.setStatus(newStatus);

    return rideRepository.save(ride);
}

private boolean isValidTransition(RideStatus currentStatus, RideStatus newStatus) {

    if (currentStatus == RideStatus.REQUESTED) {
        return newStatus == RideStatus.ASSIGNED
                || newStatus == RideStatus.CANCELLED;
    }

    if (currentStatus == RideStatus.ASSIGNED) {
        return newStatus == RideStatus.ACCEPTED
                || newStatus == RideStatus.CANCELLED;
    }

    if (currentStatus == RideStatus.ACCEPTED) {
        return newStatus == RideStatus.IN_PROGRESS
                || newStatus == RideStatus.CANCELLED;
    }

    if (currentStatus == RideStatus.IN_PROGRESS) {
        return newStatus == RideStatus.COMPLETED
                || newStatus == RideStatus.CANCELLED;
    }

    return false;
    }
}