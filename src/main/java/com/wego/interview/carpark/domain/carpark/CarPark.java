package com.wego.interview.carpark.domain.carpark;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "car_park")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Setter
@Getter
public class CarPark {
	@Id
	private String id;

	@Column(name = "address")
	private String address;

	@NotNull(message = "Coordinate of car park must be non-null.")
	@Column(name = "location", columnDefinition = "geometry(Point,4326)")
	private Point location;
}
