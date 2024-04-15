package com.wego.interview.carpark.outbound.client;

import com.wego.interview.carpark.BaseIntegrationTest;
import com.wego.interview.carpark.domain.available.AvailableCarPark;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

class SingaporeCarParkAvailabilityClientIT extends BaseIntegrationTest {

    @Autowired
    private SingaporeCarParkAvailabilityClient client;

    @Test
    void loadContext() {
    }

    @Test
    void testFetchAvailableCarParks_shouldDeserializeResponseProperly() throws IOException {
        // when
        CarParkAvailabilityResponse response = client.fetchAvailableCarParks();

        // then
        Assertions.assertThat(response.getItems()).isNotEmpty();
        Assertions.assertThat(response.getItems().get(0).getCarParkData()).isNotEmpty();
        Assertions.assertThat(response.getItems().get(0).getCarParkData().get(0).getCarParkInfo()).isNotEmpty();

        CarParkAvailabilityResponse.CarParkInfo A1CarPark = response.getItems().get(0).getCarParkData()
                .stream()
                .filter(data -> data.getCarParkNumber().equals("A1"))
                .findFirst()
                .get()
                .getCarParkInfo().get(0);

        Assertions.assertThat(A1CarPark.getTotalLots()).isEqualTo(105);
        Assertions.assertThat(A1CarPark.getLotsAvailable()).isEqualTo(101);
    }

    @Test
    void testFindAvailableCarParks() throws IOException {
        // when
        List<AvailableCarPark> availableCarParks = client.findAvailableCarParks();

        // then
        Assertions.assertThat(availableCarParks).isNotEmpty();
        AvailableCarPark A1CarPark = availableCarParks.stream()
                .filter(availableCarPark -> availableCarPark.getCarParkId().equals("A1"))
                .findFirst()
                .get();
        Assertions.assertThat(A1CarPark.getTotalLots()).isEqualTo(105);
        Assertions.assertThat(A1CarPark.getAvailableLots()).isEqualTo(101);
    }
}
