package com.ridelink.driver_vehicle_service.controller;

import com.ridelink.driver_vehicle_service.entity.Driver;
import com.ridelink.driver_vehicle_service.service.DriverService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping
    public List<Driver> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @GetMapping("/available")
    public List<Driver> getAvailableDrivers(@RequestParam String location) {
        return driverService.getAvailableDrivers(location);
    }

    @PostMapping
    public Driver createDriver(@Valid @RequestBody Driver driver) {
        return driverService.createDriver(driver);
    }

    @PutMapping("/{id}")
    public Driver updateDriver(@PathVariable Long id, @RequestBody Driver driver) {
        return driverService.updateDriver(id, driver);
    }

    @DeleteMapping("/{id}")
    public void deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
    }

    @PutMapping("/{driverId}/vehicle/{vehicleId}")
    public Driver assignVehicle(
        @PathVariable Long driverId,
        @PathVariable Long vehicleId) {

        return driverService.assignVehicle(driverId, vehicleId);
    }


}