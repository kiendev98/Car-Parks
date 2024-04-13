package com.wego.interview.carpark.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.wego.interview.carpark.config.MockServerConfig;
import com.wego.interview.carpark.domain.available.AvailableCarPark;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import wiremock.org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@SpringBootTest
@ActiveProfiles("client_test")
@ContextConfiguration(classes = { MockServerConfig.class })
class SingaporeCarParkAvailabilityClientIT {

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private SingaporeCarParkAvailabilityClient client;

    @Autowired
    private ResourceLoader resourceLoader;

    @AfterEach
    void cleanup() {
        wireMockServer.resetAll();
    }

    @Test
    void loadContext() {
    }

    @Test
    void testFetchAvailableCarParks_shouldDeserializeResponseProperly() throws IOException {
        // given
        setupWireMock();

        // when
        CarParkAvailabilityResponse response = client.fetchAvailableCarParks();

        // then
        Assertions.assertThat(response.getItems()).isNotEmpty();
        Assertions.assertThat(response.getItems().get(0).getCarParkData()).isNotEmpty();
        Assertions.assertThat(response.getItems().get(0).getCarParkData().get(0).getCarParkInfo()).isNotEmpty();

        CarParkAvailabilityResponse.CarParkInfo HE12CarParkInfo = response.getItems().get(0).getCarParkData()
                .stream()
                .filter(data -> data.getCarParkNumber().equals("HE12"))
                .findFirst()
                .get()
                .getCarParkInfo().get(0);

        Assertions.assertThat(HE12CarParkInfo.getTotalLots()).isEqualTo(105);
        Assertions.assertThat(HE12CarParkInfo.getLotsAvailable()).isEqualTo(101);
    }

    @Test
    void testFindAvailableCarParks() throws IOException {
        // given
        setupWireMock();

        // when
        List<AvailableCarPark> availableCarParks = client.findAvailableCarParks();

        // then
        Assertions.assertThat(availableCarParks).isNotEmpty();
        AvailableCarPark HE12CarPark = availableCarParks.stream()
                .filter(availableCarPark -> availableCarPark.getCarParkId().equals("HE12"))
                .findFirst()
                .get();
        Assertions.assertThat(HE12CarPark.getTotalLots()).isEqualTo(105);
        Assertions.assertThat(HE12CarPark.getAvailableLots()).isEqualTo(101);
    }

    private void setupWireMock() throws IOException {
        wireMockServer.stubFor(
                WireMock.get(WireMock.urlEqualTo("/transport/carpark-availability"))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(HttpStatus.OK_200)
                                        .withHeader("content-type", "application/json")
                                        .withBody(resourceLoader.getResource("classpath:client/mockClientResponse.json").getContentAsString(Charset.defaultCharset()))
                        )
        );
        wireMockServer.start();
    }
}
