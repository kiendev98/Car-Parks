package com.wego.interview.carpark.domain.carpark;

import com.wego.interview.carpark.domain.available.AvailableCarPark;
import com.wego.interview.carpark.domain.available.CarParkQueryService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;

import java.util.List;
import java.util.Objects;

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
     * @param page The page request
     *
     * @return The list of available car parks that is paginated.
     */
    public List<CarPark> findNearestAvailableCarParks(Coordinate coordinate, NearestCarParkPage page) {
        if (Objects.isNull(page)) {
            page = NearestCarParkPage.unpaged();
        }

        List<String> availableCarParkIds = carParkQueryService.findAvailableCarParks()
                .stream()
                .map(AvailableCarPark::getCarParkId)
                .toList();


        return carParkRepository.findNearestCarParksInIds(coordinate, availableCarParkIds, page);
    }
}
