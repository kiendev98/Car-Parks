package com.wego.interview.carpark.domain.carpark;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class NearestCarPark {
    private final String address;

    private final double latitude;

    private final double longitude;

    @JsonProperty("total_lots")
    private final int totalLots;

    @JsonProperty("available_lots")
    private final int availableLots;
}
