package com.wego.interview.carpark.domain.carpark;

import org.locationtech.jts.geom.Coordinate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Abstraction for car park repository.
 */
public interface CarParkRepository {
    /**
     * Find nearest carpark in the list of car park ids.
     *
     * @param coordinate The central point to find nearest car parks.
     * @param carParkIds The list of car park ids for filtering
     * @param pageable page request
     *
     * @return The paginated result.
     */
    Page<CarPark> findNearestCarParksInIds(Coordinate coordinate, List<String> carParkIds, Pageable pageable);
}
