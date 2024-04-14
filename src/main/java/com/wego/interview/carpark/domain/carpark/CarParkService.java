package com.wego.interview.carpark.domain.carpark;

import com.wego.interview.carpark.domain.available.AvailableCarPark;
import com.wego.interview.carpark.domain.available.CarParkQueryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service for car park domain.
 */
@RequiredArgsConstructor
@Component
@Slf4j
@Transactional
public class CarParkService {

    private final CarParkQueryService carParkQueryService;
    private final CarParkRepository carParkRepository;

    /**
     * Find nearest available car parks.
     *
     * @param coordinate The coordinate to find nearby
     * @param page The page request
     *
     * @return The list of available car parks that is paginated.
     */
    public List<NearestCarPark> findNearestAvailableCarParks(Coordinate coordinate, NearestCarParkPage page) {
        StopWatch stopWatch = StopWatch.createStarted();
        if (Objects.isNull(page)) {
            page = NearestCarParkPage.unpaged();
        }

        Map<String, AvailableCarPark> availableCarParks = carParkQueryService.findAvailableCarParks()
                .stream()
                .distinct()
                .collect(Collectors.toMap(AvailableCarPark::getCarParkId, Function.identity()));

        List<NearestCarPark> nearestCarParks = carParkRepository.findNearestCarParksInIds(coordinate, availableCarParks.keySet(), page)
                .stream()
                .map(carPark -> toNearestCarPark(
                        carPark,
                        availableCarParks.get(carPark.getId())
                ))
                .toList();

        log.info("Finding nearest available car parks took [{}ms]", stopWatch.getTime(TimeUnit.MILLISECONDS));
        return nearestCarParks;
    }

    public List<NearestCarPark> findNearestAvailableCarParks(Coordinate coordinate) {
        return findNearestAvailableCarParks(coordinate, NearestCarParkPage.unpaged());
    }

    private NearestCarPark toNearestCarPark(CarPark carPark, AvailableCarPark availableCarPark) {
        return NearestCarPark.builder()
                .longitude(carPark.getLocation().getX())
                .latitude(carPark.getLocation().getY())
                .address(carPark.getAddress())
                .availableLots(availableCarPark.getAvailableLots())
                .totalLots(availableCarPark.getTotalLots())
                .build();
    }
}
