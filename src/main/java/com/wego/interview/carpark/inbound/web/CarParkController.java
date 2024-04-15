package com.wego.interview.carpark.inbound.web;

import com.wego.interview.carpark.domain.carpark.CarParkService;
import com.wego.interview.carpark.domain.carpark.NearestCarPark;
import com.wego.interview.carpark.domain.carpark.NearestCarParkPage;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/carparks")
@RequiredArgsConstructor
public class CarParkController {

    private final CarParkService carParkService;

    @GetMapping("/nearest")
    public List<NearestCarPark> findNearestCarPark(
            // We handle input validation our self, marking it non-required to bypass Spring validation.
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "per_page", required = false) Integer pageSize
    ) {
        if (Objects.isNull(latitude)) {
            throw new InvalidInputException("path query [latitude] must not be empty.");
        }

        if (Objects.isNull(longitude)) {
            throw new InvalidInputException("path query [longitude] must not be empty.");
        }

        if (Objects.isNull(page) && Objects.isNull(pageSize)) {
            return carParkService.findNearestAvailableCarParks(new Coordinate(longitude, latitude));
        }

        NearestCarParkPage pageRequest = makePage(page, pageSize);
        return carParkService.findNearestAvailableCarParks(new Coordinate(longitude, latitude), pageRequest);
    }

    private NearestCarParkPage makePage(Integer page, Integer pageSize) {
        InvalidInputException invalidInputException = new InvalidInputException("Invalid page request.");

        if (Objects.isNull(page) && Objects.nonNull(pageSize)) {
            throw invalidInputException;
        }

        if (Objects.nonNull(page) && Objects.isNull(pageSize)) {
            throw invalidInputException;
        }

        try {
            return NearestCarParkPage.of(page - 1, pageSize);
        } catch (Exception ex) {
            throw invalidInputException;
        }
    }
}
