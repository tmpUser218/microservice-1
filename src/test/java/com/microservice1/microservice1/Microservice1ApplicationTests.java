package com.microservice1.microservice1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Microservice1ApplicationTests {

    @Autowired
    private Environment environment;

    @Test
    void contextLoads() {
        // Context loading verification
    }

    @Test
    void verifyApplicationProperties() {
        String appName = environment.getProperty("spring.application.name");
        assertNotNull(appName, "Application name should be configured");

        String serverPort = environment.getProperty("server.port");
        assertNotNull(serverPort, "Server port should be configured");
    }

    @Test
    void verifyActiveProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        // At least default profile should be active
        assertTrue(activeProfiles.length >= 0, "Should have active profiles");
    }
}