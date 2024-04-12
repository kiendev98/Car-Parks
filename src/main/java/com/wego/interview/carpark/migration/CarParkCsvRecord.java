package com.wego.interview.carpark.migration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarParkCsvRecord {
	@JsonProperty("car_park_no")
	private String carParkNo;

	@JsonProperty("address")
	private String address;

	@JsonProperty("x_coord")
	private double xCoord;

	@JsonProperty("y_coord")
	private double yCoord;
}