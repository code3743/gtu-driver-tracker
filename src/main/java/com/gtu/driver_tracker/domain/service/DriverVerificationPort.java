package com.gtu.driver_tracker.domain.service;

public interface DriverVerificationPort {

    /**
     * Verifies the driver's identity.
     *
     * @param driverId the ID of the driver to verify
     * @return true if the driver is verified, false otherwise
     */
    boolean verifyDriver(Long driverId);
}
