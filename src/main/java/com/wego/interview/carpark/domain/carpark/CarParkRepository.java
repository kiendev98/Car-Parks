package com.wego.interview.carpark.domain.carpark;

import org.locationtech.jts.geom.Coordinate;

import java.util.List;
import java.util.Set;

/**
 * Abstraction for car park repository.
 */
public interface CarParkRepository {
    /**
     * Find nearest carpark in the list of car park ids.
     *
     * @param coordinate The central point to find nearest car parks.
     * @param carParkIds The set of car park ids for filtering
     * @param page The page request
     *
     * @return The paginated result.
     */
    List<CarPark> findNearestCarParksInIds(Coordinate coordinate, Set<String> carParkIds, NearestCarParkPage page);
}
