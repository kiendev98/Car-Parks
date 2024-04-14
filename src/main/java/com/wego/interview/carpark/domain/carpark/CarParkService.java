package com.wego.interview.carpark.domain.carpark;

import com.wego.interview.carpark.domain.available.AvailableCarPark;
import com.wego.interview.carpark.domain.available.CarParkQueryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service for car park domain.
 */
@RequiredArgsConstructor
@Component
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
        if (Objects.isNull(page)) {
            page = NearestCarParkPage.unpaged();
        }

        Map<String, AvailableCarPark> availableCarParks = carParkQueryService.findAvailableCarParks()
                .stream()
                .distinct()
                .collect(Collectors.toMap(AvailableCarPark::getCarParkId, Function.identity()));

        return carParkRepository.findNearestCarParksInIds(coordinate, availableCarParks.keySet(), page)
                .stream()
                .map(carPark -> toNearestCarPark(
                        carPark,
                        availableCarParks.get(carPark.getId())
                ))
                .toList();
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
