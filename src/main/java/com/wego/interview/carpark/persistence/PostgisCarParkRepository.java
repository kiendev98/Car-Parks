package com.wego.interview.carpark.persistence;

import com.wego.interview.carpark.domain.carpark.CarPark;
import com.wego.interview.carpark.domain.carpark.CarParkRepository;
import com.wego.interview.carpark.domain.carpark.NearestCarParkPage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class PostgisCarParkRepository implements CarParkRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Searching nearest available car parks by using the Nearest-Neighbour Search
     * See more <a href="https://postgis.net/workshops/postgis-intro/knn.html">here</a>
     */
    @Override
    public List<CarPark> findNearestCarParksInIds(
            Coordinate coordinate,
            List<String> carParkIds,
            NearestCarParkPage page
    ) {

        if (Objects.isNull(page)) {
            throw new IllegalArgumentException("Pagination must be non-null!");
        }

        Session session = entityManager.unwrap(Session.class);

        NativeQuery<CarPark> query = session.createNativeQuery(
                        "select cp.id, cp.address, " +
                                "st_transform(cp.location, 4326) as location, " +
                                "st_distance(cp.location, st_setsrid(st_makepoint(:lon, :lat), 4326)) as dist " +
                                "from car_park cp " +
                                "where cp.id in (:ids) " +
                                "order by dist asc " +
                                "offset :offset " +
                                "limit :limit"
                        , CarPark.class)
                .setParameter("lon", coordinate.getX())
                .setParameter("lat", coordinate.getY())
                .setParameterList("ids", carParkIds)
                .setParameter("offset", page.getOffset())
                .setParameter("limit", page.getPageSize());

        return query.getResultList();
    }
}
