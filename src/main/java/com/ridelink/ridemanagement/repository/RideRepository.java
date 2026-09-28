package com.ridelink.ridemanagement.repository;

import com.ridelink.ridemanagement.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride, String> {

}