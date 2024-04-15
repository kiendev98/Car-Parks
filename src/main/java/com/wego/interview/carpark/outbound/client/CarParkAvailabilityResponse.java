package com.wego.interview.carpark.outbound.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class CarParkAvailabilityResponse {
    @JsonProperty("items")
    private List<CarParkAvailabilityItem> items = new LinkedList<>();

    @Data
    public static class CarParkAvailabilityItem {
        @JsonProperty("carpark_data")
        private List<CarParkData> carParkData = new LinkedList<>();
    }

    @Data
    public static class CarParkData {
        @JsonProperty("carpark_info")
        private List<CarParkInfo> carParkInfo = new LinkedList<>();

        @JsonProperty("carpark_number")
        private String carParkNumber;
    }

    @Data
    public static class CarParkInfo {
        @JsonProperty("total_lots")
        private int totalLots;
        @JsonProperty("lots_available")
        private int lotsAvailable;
    }
}
