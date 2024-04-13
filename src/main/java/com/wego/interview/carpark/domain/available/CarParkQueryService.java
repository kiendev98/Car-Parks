package com.wego.interview.carpark.domain.available;

import java.util.List;

/**
 * Abstraction for querying the available car park information.
 */
public interface CarParkQueryService {
    /**
     * Find all available car park information.
     * @return The list of available car park.
     */
    List<AvailableCarPark> findAvailableCarParks();
}
