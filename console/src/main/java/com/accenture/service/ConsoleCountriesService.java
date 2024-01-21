package com.accenture.service;

import com.accenture.client.ConsoleRestClient;
import com.accenture.dto.Country;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsoleCountriesService extends AbstractCountriesService {

    @Override
    public List<Country> getAllCountries() {
        Optional<Country[]> response = ConsoleRestClient.INSTANCE.get(
                "/all", Country[].class, Map.of("fields", "name,population,borders,region"));
        return response.map(Arrays::asList).orElse(Collections.emptyList());
    }

    @Override
    public List<Country> getCountriesInRegion(String region) {
        Optional<Country[]> response = ConsoleRestClient.INSTANCE.get("/region/" + region, Country[].class, Map.of());
        return response.map(Arrays::asList).orElse(Collections.emptyList());
    }

    @Override
    public List<Country> getCountriesByCodes(String codes) {
        Optional<Country[]> response =
                ConsoleRestClient.INSTANCE.get("/alpha", Country[].class, Map.of("codes", codes));
        return response.map(Arrays::asList).orElse(Collections.emptyList());
    }
}
