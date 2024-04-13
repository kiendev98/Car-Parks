package com.wego.interview.carpark.client;

import com.wego.interview.carpark.domain.available.AvailableCarPark;
import com.wego.interview.carpark.domain.available.CarParkQueryService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@FeignClient(name = "carpark")
public interface SingaporeCarParkAvailabilityClient extends CarParkQueryService {
    default List<AvailableCarPark> findAvailableCarParks() {
        CarParkAvailabilityResponse response = fetchAvailableCarParks();

        if (
                response.getItems().isEmpty() ||
                        response.getItems().get(0).getCarParkData().isEmpty()
        ) {
            return Collections.emptyList();
        }

        List<CarParkAvailabilityResponse.CarParkData> carParkData = response.getItems().get(0).getCarParkData();

        return carParkData.stream()
                .filter(data -> !data.getCarParkInfo().isEmpty()) // ignore invalid data.
                .map(data -> {
                    CarParkAvailabilityResponse.CarParkInfo carParkInfo = data.getCarParkInfo().get(0);
                    return AvailableCarPark.builder()
                            .carParkId(data.getCarParkNumber())
                            .availableLots(carParkInfo.getLotsAvailable())
                            .totalLots(carParkInfo.getTotalLots())
                            .build();
                })
                .toList();
    }

    @GetMapping(value = "/transport/carpark-availability", consumes = "application/json")
    CarParkAvailabilityResponse fetchAvailableCarParks();
}
