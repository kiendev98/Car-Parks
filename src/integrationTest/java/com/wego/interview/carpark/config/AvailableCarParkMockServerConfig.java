package com.wego.interview.carpark.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import wiremock.org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.Charset;

@TestConfiguration
@ActiveProfiles("client_test")
public class AvailableCarParkMockServerConfig {
    public static final int MOCK_SERVER_PORT = 9095;

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer mockServer() throws IOException {
        WireMockServer wireMockServer = new WireMockServer(MOCK_SERVER_PORT);
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/transport/carpark-availability"))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(HttpStatus.OK_200)
                                        .withHeader("content-type", "application/json")
                                        .withBody(resourceLoader.getResource("classpath:data/mockClientResponse.json").getContentAsString(Charset.defaultCharset()))
                        )
        );
        wireMockServer.start();
        return wireMockServer;
    }

}
