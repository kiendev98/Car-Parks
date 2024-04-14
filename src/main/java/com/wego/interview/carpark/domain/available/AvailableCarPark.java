package com.wego.interview.carpark.domain.available;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode(of = { "carParkId" })
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class AvailableCarPark implements Serializable {
    private final String carParkId;
    private final int totalLots;
    private final int availableLots;
}
