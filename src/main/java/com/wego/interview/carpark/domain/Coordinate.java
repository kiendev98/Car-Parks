package com.wego.interview.carpark.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Embeddable
public class Coordinate {

	@Column(name = "longitude")
	private double longitude;

	@Column(name = "latitude")
	private double latitude;
}
