package com.gtu.driver_tracker.config;


public class RabbitMQConfig {

    private RabbitMQConfig() {
    }

    public static final String DRIVER_EXCHANGE = "drivers.exchange";

    public static final String DRIVER_ROUTING_KEY = "drivers.routingkey";

}
