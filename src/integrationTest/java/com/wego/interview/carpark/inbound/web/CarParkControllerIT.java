package com.wego.interview.carpark.inbound.web;

import com.wego.interview.carpark.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CarParkControllerIT extends BaseIntegrationTest {

    private MockMvc client;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        super.clearData();
        client = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldReturnNearestFirstTenNearestCarPark_whenPagingIsMissing() throws Exception {
        // given
        saveTestCarpark("A1", "Address 1", 0.01, 0);
        saveTestCarpark("A2", "Address 2", 0.02, 0);
        saveTestCarpark("A3", "Address 3", 0.03, 0);
        saveTestCarpark("A4", "Address 4", 0.04, 0);
        saveTestCarpark("A5", "Address 5", 0.05, 0);
        saveTestCarpark("A6", "Address 6", 0.06, 0);
        saveTestCarpark("A7", "Address 7", 0.07, 0);
        saveTestCarpark("A8", "Address 8", 0.08, 0);
        saveTestCarpark("A9", "Address 9", 0.09, 0);
        saveTestCarpark("A10", "Address 10", 0.1, 0);
        saveTestCarpark("A11", "Address 11", 0.11, 0);
        saveTestCarpark("A12", "Address 12", 0.12, 0);

        // when/then
        getNearestCarParks(
                Map.of(
                        "longitude", String.valueOf(0.0),
                        "latitude", String.valueOf(0.0)
                ))
                .andExpect(status().isOk())
                .andExpectAll(
                    jsonPath("$.length()").value(10),
                    jsonPath("$[0].address").value("Address 1"),
                    jsonPath("$[1].address").value("Address 2"),
                    jsonPath("$[2].address").value("Address 3"),
                    jsonPath("$[3].address").value("Address 4"),
                    jsonPath("$[4].address").value("Address 5"),
                    jsonPath("$[5].address").value("Address 6"),
                    jsonPath("$[6].address").value("Address 7"),
                    jsonPath("$[7].address").value("Address 8"),
                    jsonPath("$[8].address").value("Address 9"),
                    jsonPath("$[9].address").value("Address 10")
                );
    }

    @Test
    void shouldReturnNearestCarPark_withPaginatedResult() throws Exception {
        // given
        saveTestCarpark("A2", "Address 2", 0.02, 0);
        saveTestCarpark("A3", "Address 3", 0.03, 0);
        saveTestCarpark("A4", "Address 4", 0.04, 0);
        saveTestCarpark("A5", "Address 5", 0.05, 0);
        saveTestCarpark("A6", "Address 6", 0.06, 0);

        // when/then query the first page
        getNearestCarParks(
                Map.of(
                        "longitude", String.valueOf(0.07),
                        "latitude", String.valueOf(0.0),
                        "page", "1",
                        "per_page", "2"
                )).andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.length()").value(2),
                        jsonPath("$[0].address").value("Address 6"),
                        jsonPath("$[1].address").value("Address 5")
                );

        // when/then query the second page
        getNearestCarParks(
                Map.of(
                        "longitude", String.valueOf(0.07),
                        "latitude", String.valueOf(0.0),
                        "page", "2",
                        "per_page", "2"
                )).andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.length()").value(2),
                        jsonPath("$[0].address").value("Address 4"),
                        jsonPath("$[1].address").value("Address 3")
                );

        // when/then query the third/last page
        getNearestCarParks(
                Map.of(
                        "longitude", String.valueOf(0.07),
                        "latitude", String.valueOf(0.0),
                        "page", "3",
                        "per_page", "2"
                )).andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.length()").value(1),
                        jsonPath("$[0].address").value("Address 2")
                );
    }

    @Test
    void shouldReturnBadRequest_whenCoordinateIsInvalid() throws Exception {
        // given
        saveTestCarpark("A2", "Address 2", 0.02, 0);
        saveTestCarpark("A3", "Address 3", 0.03, 0);

        // when/then longitude is missing
        getNearestCarParks(
                Map.of(
                        "latitude", String.valueOf(0.04)
                ))
                .andExpect(status().isBadRequest())
                .andExpectAll(
                        jsonPath("$.path").value("/carparks/nearest"),
                        jsonPath("$.message").value("path query [longitude] must not be empty.")
                );

        // when/then latitude is missing
        getNearestCarParks(
                Map.of(
                        "longitude", String.valueOf(0.04)
                ))
                .andExpect(status().isBadRequest())
                .andExpectAll(
                        jsonPath("$.path").value("/carparks/nearest"),
                        jsonPath("$.message").value("path query [latitude] must not be empty.")
                );
    }

    @Test
    void shouldReturnBadRequest_whenPagingIsInvalid() throws Exception {
        // given
        saveTestCarpark("A2", "Address 2", 0.02, 0);
        saveTestCarpark("A3", "Address 3", 0.03, 0);

        // when/then page is missing
        getNearestCarParks(
                Map.of(
                        "latitude", String.valueOf(0.0),
                        "longitude", String.valueOf(0.04),
                        "per_page", "1"
                ))
                .andExpect(status().isBadRequest())
                .andExpectAll(
                        jsonPath("$.path").value("/carparks/nearest"),
                        jsonPath("$.message").value("Invalid page request.")
                );

        // when/then per_page is missing
        getNearestCarParks(
                Map.of(
                        "latitude", String.valueOf(0.0),
                        "longitude", String.valueOf(0.04),
                        "page", "1"
                ))
                .andExpect(status().isBadRequest())
                .andExpectAll(
                        jsonPath("$.path").value("/carparks/nearest"),
                        jsonPath("$.message").value("Invalid page request.")
                );
    }

    private ResultActions getNearestCarParks(Map<String, String> queries) throws Exception {
        LinkedMultiValueMap<String, String> queryMap = new LinkedMultiValueMap<>();
        queries.forEach(queryMap::add);

        return client.perform(
                    get("/carparks/nearest")
                            .queryParams(queryMap)
                )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
