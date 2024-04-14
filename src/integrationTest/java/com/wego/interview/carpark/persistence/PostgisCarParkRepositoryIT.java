package com.wego.interview.carpark.persistence;

import com.wego.interview.carpark.BaseIntegrationTest;
import com.wego.interview.carpark.domain.carpark.CarPark;
import com.wego.interview.carpark.domain.carpark.NearestCarParkPage;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
class PostgisCarParkRepositoryIT extends BaseIntegrationTest {

    @Autowired
    private PostgisCarParkRepository postgisCarParkRepository;

    @Test
    void testFindNearestCarParksInIds_shouldSortTheResultByDistance() {
        // given
        Coordinate leftPoint = new Coordinate(0, 0); // left point of saved points.
        Coordinate rightPoint = new Coordinate(7, 0); // right point of saved points.
        saveTestCarpark("A1", "Address1", 1, 0);
        saveTestCarpark("A2", "Address2", 2, 0);
        saveTestCarpark("A3", "Address3", 3, 0);
        saveTestCarpark("A4", "Address4", 4, 0);

        // when query from left to right
        List<CarPark> nearestCarParks = postgisCarParkRepository.findNearestCarParksInIds(leftPoint, Set.of("A3", "A2", "A1", "A4"), NearestCarParkPage.unpaged());

        // then
        Assertions.assertThat(nearestCarParks).hasSize(4);
        Assertions.assertThat(nearestCarParks.get(0).getId()).isEqualTo("A1");
        Assertions.assertThat(nearestCarParks.get(1).getId()).isEqualTo("A2");
        Assertions.assertThat(nearestCarParks.get(2).getId()).isEqualTo("A3");
        Assertions.assertThat(nearestCarParks.get(3).getId()).isEqualTo("A4");


        // when query from right to left
        nearestCarParks = postgisCarParkRepository.findNearestCarParksInIds(rightPoint, Set.of("A3", "A2", "A1", "A4"), NearestCarParkPage.unpaged());

        // then
        Assertions.assertThat(nearestCarParks).hasSize(4);
        Assertions.assertThat(nearestCarParks.get(0).getId()).isEqualTo("A4");
        Assertions.assertThat(nearestCarParks.get(1).getId()).isEqualTo("A3");
        Assertions.assertThat(nearestCarParks.get(2).getId()).isEqualTo("A2");
        Assertions.assertThat(nearestCarParks.get(3).getId()).isEqualTo("A1");
    }

    @Test
    void testFindNearestCarParksInIds_shouldOnlyReturnsCarParkInIdsInput() {
        // given
        Coordinate centralPoint = new Coordinate(0, 0);
        saveTestCarpark("A4", "Address 4", 0, 0.4);
        saveTestCarpark("A1", "Address 1", 0, 0.1);

        // when
        List<CarPark> nearestCarParks = postgisCarParkRepository.findNearestCarParksInIds(centralPoint, Set.of(), NearestCarParkPage.unpaged());

        // then
        Assertions.assertThat(nearestCarParks).isEmpty();

        // and when
        nearestCarParks = postgisCarParkRepository.findNearestCarParksInIds(centralPoint, Set.of("A6"), NearestCarParkPage.unpaged());

        // then
        Assertions.assertThat(nearestCarParks).isEmpty();

        // and when
        nearestCarParks = postgisCarParkRepository.findNearestCarParksInIds(centralPoint, Set.of("A4"), NearestCarParkPage.unpaged());

        // then
        Assertions.assertThat(nearestCarParks).hasSize(1);
        Assertions.assertThat(nearestCarParks.get(0).getId()).isEqualTo("A4");

    }

    @Test
    void testFindNearestCarParksInIds_shouldReturnPaginatedData() {
        // given
        Coordinate leftPoint = new Coordinate(0, 0);
        saveTestCarpark("A1", "Address1", 0.01, 0);
        saveTestCarpark("A2", "Address2", 0.02, 0);
        saveTestCarpark("A3", "Address3", 0.03, 0);
        saveTestCarpark("A4", "Address4", 0.04, 0);
        saveTestCarpark("A5", "Address5", 0.04, 0);

        // when
        List<CarPark> nearestCarParks = postgisCarParkRepository.findNearestCarParksInIds(leftPoint, Set.of("A1", "A2", "A3", "A4"), NearestCarParkPage.of(0, 2));

        // then
        Assertions.assertThat(nearestCarParks).hasSize(2);
        Assertions.assertThat(nearestCarParks.get(0).getId()).isEqualTo("A1");
        Assertions.assertThat(nearestCarParks.get(1).getId()).isEqualTo("A2");

        // and when
        nearestCarParks = postgisCarParkRepository.findNearestCarParksInIds(leftPoint, Set.of("A1", "A2", "A3", "A4", "A5"), NearestCarParkPage.of(1, 2));

        // then
        Assertions.assertThat(nearestCarParks).hasSize(2);
        Assertions.assertThat(nearestCarParks.get(0).getId()).isEqualTo("A3");
        Assertions.assertThat(nearestCarParks.get(1).getId()).isEqualTo("A4");
    }

    @Test
    void testFindNearestCarParksInIds_shouldThrowExceptionWhenPageInputIsNull() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> postgisCarParkRepository.findNearestCarParksInIds(new Coordinate(0,0), Set.of("A1"), null));
    }
}