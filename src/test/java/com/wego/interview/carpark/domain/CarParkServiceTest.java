package com.wego.interview.carpark.domain;

import com.wego.interview.carpark.domain.available.AvailableCarPark;
import com.wego.interview.carpark.domain.available.CarParkQueryService;
import com.wego.interview.carpark.domain.carpark.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.*;

class CarParkServiceTest {

    private CarParkQueryService carParkQueryService;
    private CarParkRepository carParkRepository;
    private CarParkService carParkService;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    @BeforeEach
    public void setup() {
        carParkQueryService = mock(CarParkQueryService.class);
        carParkRepository = mock(CarParkRepository.class);
        carParkService = new CarParkService(carParkQueryService, carParkRepository);
    }

    @Test
    void testFindNearestCarParks_withPaging() {
        // given
        NearestCarParkPage pageRequest = NearestCarParkPage.unpaged();
        Coordinate coordinate = new Coordinate(0, 0);
        Set<String> availableCarParkIds = Set.of("A1", "A2");
        List<AvailableCarPark> availableCarParks = List.of(
                AvailableCarPark.builder()
                        .carParkId("A1")
                        .availableLots(1)
                        .totalLots(1)
                        .build(),
                AvailableCarPark.builder()
                        .carParkId("A2")
                        .availableLots(2)
                        .totalLots(2)
                        .build()
        );
        List<CarPark> carParks = List.of(
                CarPark.builder()
                        .id("A1")
                        .location(geometryFactory.createPoint(new Coordinate(1, 1)))
                        .address("Address 1")
                        .build(),
                CarPark.builder()
                        .id("A2")
                        .location(geometryFactory.createPoint(new Coordinate(2, 2)))
                        .address("Address 2")
                        .build()
        );

        when(carParkQueryService.findAvailableCarParks()).thenReturn(availableCarParks);
        when(carParkRepository.findNearestCarParksInIds(coordinate, availableCarParkIds, pageRequest)).thenReturn(carParks);

        // when
        List<NearestCarPark> result = carParkService.findNearestAvailableCarParks(coordinate, pageRequest);

        // then
        Assertions.assertArrayEquals(
                List.of(
                        NearestCarPark.builder()
                                .availableLots(1)
                                .totalLots(1)
                                .longitude(1)
                                .latitude(1)
                                .address("Address 1")
                                .build(),
                        NearestCarPark.builder()
                                .availableLots(2)
                                .totalLots(2)
                                .longitude(2)
                                .latitude(2)
                                .address("Address 2")
                                .build()
                ).toArray(),
                result.toArray()
        );
    }

    @Test
    void testFindNearestCarParks_shouldEnforcePaginationToFirstTenResult() {
        //given
        AtomicInteger index = new AtomicInteger(1);
        Coordinate coordinate = new Coordinate(0, 0);
        Set<String> availableCarParkIds = Set.of("A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "A10", "A11");
        List<AvailableCarPark> availableCarParks = availableCarParkIds.stream()
                .map(id -> new AvailableCarPark(id, index.get(), index.getAndIncrement()))
                .toList();

        when(carParkQueryService.findAvailableCarParks()).thenReturn(availableCarParks);

        // when
        carParkService.findNearestAvailableCarParks(coordinate);

        // then
        verify(carParkRepository).findNearestCarParksInIds(
                any(),
                any(),
                argThat(page -> page.getPageSize() == 10 && page.getPage() == 0)
        );
    }
}
