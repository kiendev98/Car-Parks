package com.wego.interview.carpark.domain.carpark;

import com.wego.interview.carpark.domain.available.AvailableCarPark;
import com.wego.interview.carpark.domain.available.CarParkQueryService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service abstract for car park domain.
 */
@RequiredArgsConstructor
public class CarParkService {

    private final CarParkQueryService carParkQueryService;
    private final CarParkRepository carParkRepository;

    /**
     * Find nearest available car parks.
     *
     * @param coordinate The coordinate to find nearby
     * @param pageable Page request
     *
     * @return The list of available car parks that is paginated.
     */
    public List<CarPark> findNearestAvailableCarParks(Coordinate coordinate, Pageable pageable) {
        List<String> availableCarParkIds = carParkQueryService.findAvailableCarParks()
                .stream()
                .map(AvailableCarPark::getCarParkId)
                .toList();

        return carParkRepository.findNearestCarParksInIds(coordinate, availableCarParkIds, pageable)
                .getContent();
    }
}
