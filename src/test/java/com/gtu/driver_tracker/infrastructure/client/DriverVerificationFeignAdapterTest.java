package com.gtu.driver_tracker.infrastructure.client;

import com.gtu.driver_tracker.domain.model.Driver;
import com.gtu.driver_tracker.infrastructure.dto.ApiResponse;
import com.gtu.driver_tracker.infrastructure.dto.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DriverVerificationFeignAdapterTest {

    private DriverServiceClient client;
    private DriverVerificationFeignAdapter adapter;

    @BeforeEach
    void setUp() {
        client = mock(DriverServiceClient.class);
        adapter = new DriverVerificationFeignAdapter(client);
    }

    @Test
    void getDriverById_shouldReturnDriver_whenUserIsDriver() {
        Long driverId = 1L;
        UserResponse userData = new UserResponse(driverId, "Jane Doe", "jane@example.com", "DRIVER");
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>("Success", userData, 200);

        when(client.getUserById(driverId)).thenReturn(apiResponse);

        Driver driver = adapter.getDriverById(driverId);

        assertNotNull(driver);
        assertEquals(driverId, driver.getId());
        assertEquals("Jane Doe", driver.getName());
    }

    @Test
    void getDriverById_shouldReturnNull_whenUserIsNotDriver() {
        Long driverId = 2L;
        UserResponse userData = new UserResponse(driverId, "John Smith", "john@example.com", "PASSENGER");
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>("Success", userData, 200);

        when(client.getUserById(driverId)).thenReturn(apiResponse);

        Driver driver = adapter.getDriverById(driverId);

        assertNull(driver);
    }

    @Test
    void getDriverById_shouldReturnNull_whenUserIsNull() {
        Long driverId = 3L;

        when(client.getUserById(driverId)).thenReturn(null);

        Driver driver = adapter.getDriverById(driverId);

        assertNull(driver);
    }

    @Test
    void getDriverById_shouldReturnNull_whenExceptionIsThrown() {
        Long driverId = 4L;

        when(client.getUserById(driverId)).thenThrow(new RuntimeException("Service unavailable"));

        Driver driver = adapter.getDriverById(driverId);

        assertNull(driver);
    }
}
