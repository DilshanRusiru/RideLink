package com.ridelink.driver_vehicle_service.repository;

import com.ridelink.driver_vehicle_service.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    List<Driver> findByAvailableTrueAndVehicle_AvailableTrueAndVehicle_LocationIgnoreCase(String location);

}