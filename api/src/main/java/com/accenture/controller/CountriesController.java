package com.accenture.controller;

import com.accenture.dto.Country;
import com.accenture.service.ApiCountriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Countries", description = "Countries information API")
@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountriesController {

    private final ApiCountriesService apiCountriesService;

    @Operation(
            method = "GET",
            summary = "Get all countries sorted by population.",
            description = "The list of countries are displayed in descending order")
    @GetMapping("/sortedByPopulation")
    public ResponseEntity<List<Country>> getCountriesSortedByPopulationDescending() {
        return ResponseEntity.ok(apiCountriesService.getCountriesSortedByPopulation());
    }

    @Operation(
            method = "GET",
            summary = "Get country in region with most borders outside region.",
            description =
                    "To get the country in a region that has the highest number of borders with countries that do not belong to the same region")
    @GetMapping("/{region}/countryWithMostBordersOutside")
    public ResponseEntity<Country> getCountryWithMostBordersOutsideRegion(
            @Parameter(description = "Region of the country", required = true, example = "asia") @PathVariable("region")
                    String region) {
        return ResponseEntity.ok(apiCountriesService.getCountryWithMostBordersOutsideRegion(region));
    }
}
