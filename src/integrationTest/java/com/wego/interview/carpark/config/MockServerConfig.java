package com.wego.interview.carpark.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

@TestConfiguration
@ActiveProfiles("client_test")
public class MockServerConfig {
    public static final int MOCK_SERVER_PORT = 9095;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer mockServer() {
        return new WireMockServer(MOCK_SERVER_PORT);
    }
}
