package com.skyscanner;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HoenScannerApplication extends Application<HoenScannerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new HoenScannerApplication().run(args);
    }

    @Override
    public String getName() {
        return "hoen-scanner";
    }

    @Override
    public void initialize(final Bootstrap<HoenScannerConfiguration> bootstrap) {

    }

    @Override
    public void run(final HoenScannerConfiguration configuration, final Environment environment) throws Exception {
        List<SearchResult> searchResults = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        // Load hotels.json
        try (InputStream hotelsStream = getClass().getResourceAsStream("/hotels.json")) {
            List<Map<String, String>> hotels = mapper.readValue(hotelsStream, new TypeReference<List<Map<String, String>>>() {});
            for (Map<String, String> hotel : hotels) {
                searchResults.add(new SearchResult(hotel.get("city"), "hotel", hotel.get("title")));
            }
        }

        // Load rental_cars.json
        try (InputStream carsStream = getClass().getResourceAsStream("/rental_cars.json")) {
            List<Map<String, String>> cars = mapper.readValue(carsStream, new TypeReference<List<Map<String, String>>>() {});
            for (Map<String, String> car : cars) {
                searchResults.add(new SearchResult(car.get("city"), "car", car.get("title")));
            }
        }

        // Register the search resource
        environment.jersey().register(new SearchResource(searchResults));
    }

}
