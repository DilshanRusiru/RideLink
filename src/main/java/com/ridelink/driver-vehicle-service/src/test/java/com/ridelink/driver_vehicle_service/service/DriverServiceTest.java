package com.ridelink.driver_vehicle_service.service;

import com.ridelink.driver_vehicle_service.entity.Driver;
import com.ridelink.driver_vehicle_service.entity.Vehicle;
import com.ridelink.driver_vehicle_service.repository.DriverRepository;
import com.ridelink.driver_vehicle_service.repository.VehicleRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private DriverService driverService;


    @Test
    void getAllDrivers_shouldReturnAllDrivers() {

        Driver driver1 = new Driver();
        driver1.setName("Test Driver 1");
        driver1.setPhone("0712345678");

        Driver driver2 = new Driver();
        driver2.setName("Test Driver 2");
        driver2.setPhone("0723456789");

        when(driverRepository.findAll())
                .thenReturn(List.of(driver1, driver2));

        List<Driver> result = driverService.getAllDrivers();

        assertEquals(2, result.size());
        verify(driverRepository, times(1)).findAll();
    }


    @Test
    void getAvailableDrivers_shouldReturnDriversForLocation() {

        Driver driver = new Driver();
        driver.setName("Available Driver");
        driver.setPhone("0712345678");
        driver.setAvailable(true);

        when(driverRepository
                .findByAvailableTrueAndVehicle_AvailableTrueAndVehicle_LocationIgnoreCase("Ratnapura"))
                .thenReturn(List.of(driver));

        List<Driver> result =
                driverService.getAvailableDrivers("Ratnapura");

        assertEquals(1, result.size());
        assertEquals("Available Driver", result.get(0).getName());

        verify(driverRepository, times(1))
                .findByAvailableTrueAndVehicle_AvailableTrueAndVehicle_LocationIgnoreCase("Ratnapura");
    }


    @Test
    void createDriver_shouldSaveDriver() {

        Driver driver = new Driver();
        driver.setName("Test Driver");
        driver.setPhone("0712345678");

        when(driverRepository.save(driver))
                .thenReturn(driver);

        Driver result = driverService.createDriver(driver);

        assertNotNull(result);
        assertEquals("Test Driver", result.getName());

        verify(driverRepository, times(1)).save(driver);
    }


    @Test
    void updateDriver_shouldUpdateNameAndPhone() {

        Driver existingDriver = new Driver();
        existingDriver.setName("Old Name");
        existingDriver.setPhone("0711111111");

        Driver updatedDriver = new Driver();
        updatedDriver.setName("New Name");
        updatedDriver.setPhone("0722222222");

        when(driverRepository.findById(1L))
                .thenReturn(Optional.of(existingDriver));

        when(driverRepository.save(existingDriver))
                .thenReturn(existingDriver);

        Driver result =
                driverService.updateDriver(1L, updatedDriver);

        assertEquals("New Name", result.getName());
        assertEquals("0722222222", result.getPhone());

        verify(driverRepository, times(1)).findById(1L);
        verify(driverRepository, times(1)).save(existingDriver);
    }


    @Test
    void assignVehicle_shouldAssignVehicleToDriver() {

        Driver driver = new Driver();
        driver.setName("Test Driver");
        driver.setAvailable(true);

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber("CAB-7777");
        vehicle.setVehicleType("Car");
        vehicle.setAvailable(true);
        vehicle.setLocation("Ratnapura");

        when(driverRepository.findById(1L))
                .thenReturn(Optional.of(driver));

        when(vehicleRepository.findById(7L))
                .thenReturn(Optional.of(vehicle));

        when(vehicleRepository.save(vehicle))
                .thenReturn(vehicle);

        when(driverRepository.save(driver))
                .thenReturn(driver);

        Driver result =
                driverService.assignVehicle(1L, 7L);

        assertNotNull(result);
        assertEquals(vehicle, result.getVehicle());

        assertFalse(vehicle.isAvailable());

        verify(driverRepository, times(1)).findById(1L);
        verify(vehicleRepository, times(1)).findById(7L);
        verify(vehicleRepository, times(1)).save(vehicle);
        verify(driverRepository, times(1)).save(driver);
    }

    @Test
    void assignVehicle_shouldThrowExceptionWhenDriverNotFound() {

        when(driverRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                java.util.NoSuchElementException.class,
                () -> driverService.assignVehicle(999L, 7L)
        );

        verify(driverRepository, times(1)).findById(999L);
    }
}