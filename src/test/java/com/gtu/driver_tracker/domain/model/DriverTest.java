package com.gtu.driver_tracker.domain.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class DriverTest {

    @Test
    void constructorAndGettersShouldWork() {
        Long id = 1L;
        String name = "John Doe";
        Driver driver = new Driver(id, name);

        assertEquals(id, driver.getId());
        assertEquals(name, driver.getName());
    }

    @Test
    void getIdShouldReturnCorrectValue() {
        Driver driver = new Driver(42L, "Alice");
        assertEquals(42L, driver.getId());
    }

    @Test
    void getNameShouldReturnCorrectValue() {
        Driver driver = new Driver(99L, "Bob");
        assertEquals("Bob", driver.getName());
    }
}