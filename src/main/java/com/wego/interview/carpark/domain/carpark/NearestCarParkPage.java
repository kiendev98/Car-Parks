package com.wego.interview.carpark.domain.carpark;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class NearestCarParkPage  {
    private final int page;
    private final int pageSize;

    public long getOffset() {
        return (long) page * (long) pageSize;
    }

    public NearestCarParkPage(int page, int pageSize) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be less than zero");
        }

        if (pageSize < 1) {
            throw new IllegalArgumentException("Page size must not be less than one");
        }

        this.page = page;
        this.pageSize = pageSize;
    }


    public static NearestCarParkPage ofSize(int pageSize) {
        return new NearestCarParkPage(0, pageSize);
    }

    public static NearestCarParkPage of(int page, int pageSize) {
        return new NearestCarParkPage(page, pageSize);
    }

    public static NearestCarParkPage unpaged() {
        return new NearestCarParkPage(0, 10);
    }
}
