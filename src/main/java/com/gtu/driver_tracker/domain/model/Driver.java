package com.gtu.driver_tracker.domain.model;

public class Driver {
    private Long id;
    private String name;

    public Driver(Long id, String name) {
        this.id = id;
        this.name = name;
        
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
