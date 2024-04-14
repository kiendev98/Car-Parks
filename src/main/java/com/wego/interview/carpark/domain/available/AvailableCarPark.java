package com.wego.interview.carpark.domain.available;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(of = { "carParkId" })
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class AvailableCarPark {
    private final String carParkId;
    private final int totalLots;
    private final int availableLots;
}
