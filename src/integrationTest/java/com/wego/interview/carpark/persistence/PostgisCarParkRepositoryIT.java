package com.wego.interview.carpark.persistence;

import com.wego.interview.carpark.BasePostgisIntegraionTest;
import com.wego.interview.carpark.CarParkApplication;
import com.wego.interview.carpark.domain.carpark.CarPark;
import com.wego.interview.carpark.domain.carpark.NearestCarParkPage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(
        properties = {
                "spring.liquibase.contexts=test"
        },
        classes = { CarParkApplication.class }
)
@Transactional
class PostgisCarParkRepositoryIT extends BasePostgisIntegraionTest {

    @Autowired
    private PostgisCarParkRepository postgisCarParkRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    @AfterEach
    void clearData() {
        entityManager.createNativeQuery("delete from car_park")
                .executeUpdate();
    }

    @Test
    void testFindNearestCarParksInIds_shouldSortTheResultByDistance() {
        // given
        Coordinate centralPoint = new Coordinate(0, 0);
        saveTestCarpark("A1", "Address1", 0, 1);
        saveTestCarpark("A2", "Address2", 0, 4);
        saveTestCarpark("A3", "Address3", 0, 3);
        saveTestCarpark("A4", "Address4", 0, 2);

        // when
        List<CarPark> nearestCarParks = postgisCarParkRepository.findNearestCarParksInIds(centralPoint, Arrays.asList("A1", "A2", "A3", "A4"), NearestCarParkPage.unpaged());

        // then
        Assertions.assertThat(nearestCarParks.size()).isEqualTo(4);
        Assertions.assertThat(nearestCarParks.get(0).getId()).isEqualTo("A1");
        Assertions.assertThat(nearestCarParks.get(1).getId()).isEqualTo("A4");
        Assertions.assertThat(nearestCarParks.get(2).getId()).isEqualTo("A3");
        Assertions.assertThat(nearestCarParks.get(3).getId()).isEqualTo("A2");
    }

    @Test
    void testFindNearestCarParksInIds_shouldOnlyReturnsCarParkInIdsInput() {
        // given
        Coordinate centralPoint = new Coordinate(0, 0);
        saveTestCarpark("A1", "Address1", 0, 1);
        saveTestCarpark("A2", "Address2", 0, 4);
        saveTestCarpark("A3", "Address3", 0, 3);
        saveTestCarpark("A4", "Address4", 0, 2);

        // when
        List<CarPark> nearestCarParks = postgisCarParkRepository.findNearestCarParksInIds(centralPoint, List.of(), NearestCarParkPage.unpaged());

        // then
        Assertions.assertThat(nearestCarParks).isEmpty();

        // and when
        nearestCarParks = postgisCarParkRepository.findNearestCarParksInIds(centralPoint, List.of("A5"), NearestCarParkPage.unpaged());

        // then
        Assertions.assertThat(nearestCarParks).isEmpty();

        // and when
        nearestCarParks = postgisCarParkRepository.findNearestCarParksInIds(centralPoint, List.of("A4"), NearestCarParkPage.unpaged());

        // then
        Assertions.assertThat(nearestCarParks).hasSize(1);
        Assertions.assertThat(nearestCarParks.get(0).getId()).isEqualTo("A4");

    }

    @Test
    void testFindNearestCarParksInIds_shouldReturnPaginatedData() {
        // given
        Coordinate centralPoint = new Coordinate(0, 0);
        saveTestCarpark("A1", "Address1", 4, 0);
        saveTestCarpark("A2", "Address2", 2, 0);
        saveTestCarpark("A3", "Address3", 1, 0);
        saveTestCarpark("A4", "Address4", 3, 0);

        // when
        List<CarPark> nearestCarParks = postgisCarParkRepository.findNearestCarParksInIds(centralPoint, Arrays.asList("A1", "A2", "A3", "A4"), NearestCarParkPage.of(0, 2));

        // then
        Assertions.assertThat(nearestCarParks).hasSize(2);
        Assertions.assertThat(nearestCarParks.get(0).getId()).isEqualTo("A3");
        Assertions.assertThat(nearestCarParks.get(1).getId()).isEqualTo("A2");

        // and when
        nearestCarParks = postgisCarParkRepository.findNearestCarParksInIds(centralPoint, Arrays.asList("A1", "A2", "A3", "A4"), NearestCarParkPage.of(1, 2));

        // then
        Assertions.assertThat(nearestCarParks).hasSize(2);
        Assertions.assertThat(nearestCarParks.get(0).getId()).isEqualTo("A4");
        Assertions.assertThat(nearestCarParks.get(1).getId()).isEqualTo("A1");
    }

    @Test()
    void testFindNearestCarParksInIds_shouldThrowExceptionWhenPageInputIsNull() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> postgisCarParkRepository.findNearestCarParksInIds(new Coordinate(0,0), List.of("A1"), null));
    }

    private void saveTestCarpark(String id, String address, double log, double lat)  {
        CarPark carPark = CarPark.builder()
                .id(id)
                .address(address)
                .location(geometryFactory.createPoint(new Coordinate(log, lat)))
                .build();

        entityManager.persist(carPark);
    }
}