package com.accenture.service;

import com.accenture.client.ApiRestClient;
import com.accenture.dto.Country;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiCountriesService extends AbstractCountriesService {

    private final ApiRestClient restClient;

    @Override
    public List<Country> getAllCountries() {
        Optional<Country[]> response =
                restClient.executeGet("/all", Country[].class, Map.of("fields", "name,borders,population,cioc"));
        return response.map(Arrays::asList).orElse(Collections.emptyList());
    }

    @Override
    public List<Country> getCountriesInRegion(String region) {
        Optional<Country[]> response = restClient.executeGet("/region/" + region, Country[].class, Map.of());
        return response.map(Arrays::asList).orElse(Collections.emptyList());
    }

    @Override
    public List<Country> getCountriesByCodes(String codes) {
        Optional<Country[]> response = restClient.executeGet("/alpha", Country[].class, Map.of("codes", codes));
        return response.map(Arrays::asList).orElse(Collections.emptyList());
    }
}
