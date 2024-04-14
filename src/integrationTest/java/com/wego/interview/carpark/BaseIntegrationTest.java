package com.wego.interview.carpark;

import com.wego.interview.carpark.config.AvailableCarParkMockServerConfig;
import com.wego.interview.carpark.domain.carpark.CarPark;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(
        classes = { CarParkApplication.class, AvailableCarParkMockServerConfig.class }
)
@ActiveProfiles("test")
@DirtiesContext
@Testcontainers
@Transactional
public class BaseIntegrationTest {

    @Container
    @ServiceConnection
    public final static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer(
            DockerImageName.parse("postgis/postgis:15-3.4").asCompatibleSubstituteFor("postgres")
    );

    @PersistenceContext
    private EntityManager entityManager;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    @BeforeEach
    void clearData() {
        entityManager.createNativeQuery("delete from car_park")
                .executeUpdate();
    }

    public void saveTestCarpark(String id, String address, double log, double lat)  {
        CarPark carPark = CarPark.builder()
                .id(id)
                .address(address)
                .location(geometryFactory.createPoint(new Coordinate(log, lat)))
                .build();

        entityManager.persist(carPark);
    }
}
