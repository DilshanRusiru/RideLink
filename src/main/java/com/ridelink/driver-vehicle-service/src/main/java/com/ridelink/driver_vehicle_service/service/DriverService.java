package com.ridelink.driver_vehicle_service.service;

import com.ridelink.driver_vehicle_service.entity.Driver;
import com.ridelink.driver_vehicle_service.repository.DriverRepository;
import com.ridelink.driver_vehicle_service.entity.Vehicle;
import com.ridelink.driver_vehicle_service.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    private final VehicleRepository vehicleRepository;

    public DriverService(DriverRepository driverRepository, VehicleRepository vehicleRepository) {
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Driver createDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    public Driver updateDriver(Long id, Driver driver) {
        Driver existingDriver = driverRepository.findById(id).orElseThrow();

        existingDriver.setName(driver.getName());
        existingDriver.setPhone(driver.getPhone());
        existingDriver.setAvailable(driver.isAvailable());
        existingDriver.setLocation(driver.getLocation());

        return driverRepository.save(existingDriver);
    }

    public void deleteDriver(Long id) {
        driverRepository.deleteById(id);
    }

    public List<Driver> getAvailableDrivers(String location) {
        return driverRepository.findByAvailableTrueAndVehicle_AvailableTrueAndVehicle_LocationIgnoreCase(location);
    }

    public Driver assignVehicle(Long driverId, Long vehicleId) {

        Driver driver = driverRepository.findById(driverId).orElseThrow();
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow();

        driver.setVehicle(vehicle);

        vehicle.setAvailable(false);
        vehicleRepository.save(vehicle);

        return driverRepository.save(driver);
    }


}