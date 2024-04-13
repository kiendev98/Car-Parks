package com.wego.interview.carpark.domain;

import com.wego.interview.carpark.domain.available.AvailableCarPark;
import com.wego.interview.carpark.domain.available.CarParkQueryService;
import com.wego.interview.carpark.domain.carpark.CarPark;
import com.wego.interview.carpark.domain.carpark.CarParkRepository;
import com.wego.interview.carpark.domain.carpark.CarParkService;
import com.wego.interview.carpark.domain.carpark.NearestCarParkPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CarParkServiceTest {

    private CarParkQueryService carParkQueryService;
    private CarParkRepository carParkRepository;
    private CarParkService carParkService;

    @BeforeEach
    public void setup() {
        carParkQueryService = mock(CarParkQueryService.class);
        carParkRepository = mock(CarParkRepository.class);
        carParkService = new CarParkService(carParkQueryService, carParkRepository);
    }

    @Test
    void testFindNearestCarParks() {
        // given
        AtomicInteger index = new AtomicInteger(1);
        NearestCarParkPage pageRequest = NearestCarParkPage.unpaged();
        Coordinate coordinate = new Coordinate(0, 0);
        List<String> availableCarParkIds = Arrays.asList("A1", "A2", "A3");
        List<AvailableCarPark> availableCarParks = availableCarParkIds.stream()
                        .map(id -> new AvailableCarPark(id, index.get(), index.getAndIncrement()))
                        .toList();
        List<CarPark> expectedCarParks = availableCarParks.stream()
                        .map(availableCarPark -> CarPark.builder().id(availableCarPark.getCarParkId()).build())
                        .toList();
        when(carParkQueryService.findAvailableCarParks()).thenReturn(availableCarParks);
        when(carParkRepository.findNearestCarParksInIds(coordinate, availableCarParkIds, pageRequest)).thenReturn(expectedCarParks);

        // when
        List<CarPark> result = carParkService.findNearestAvailableCarParks(coordinate, pageRequest);

        // then
        Assertions.assertArrayEquals(expectedCarParks.toArray(), result.toArray());
    }
}
