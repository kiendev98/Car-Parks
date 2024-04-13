package com.wego.interview.carpark.domain.available;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class AvailableCarPark {
    private final String carParkId;
    private final int totalLots;
    private final int availableLots;
}
