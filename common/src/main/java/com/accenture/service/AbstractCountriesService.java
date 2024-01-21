package com.accenture.service;

import com.accenture.dto.Country;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract class containing the logic for the solution
 * Intention is to have other modules extend this class and implement the abstract methods to necessary information to produce the result
 */
@Slf4j
public abstract class AbstractCountriesService {

    /**
     * Get a list of all the countries
     * @return a list of {@link Country}
     */
    public abstract List<Country> getAllCountries();

    /**
     * Get all countries in a given region
     * @param region the region to search
     * @return a list of {@link Country} residing in the given region
     */
    public abstract List<Country> getCountriesInRegion(String region);

    /**
     * Get all countries by their codes
     * @param codes list of comma-separated string of codes to search for
     * @return a list of {@link Country} based on the given codes
     */
    public abstract List<Country> getCountriesByCodes(String codes);

    /**
     * Get all countries sorted by population in descending order
     * @return list of {@link Country} sorted by population
     */
    public List<Country> getCountriesSortedByPopulation() {
        log.info("Getting countries sorted by population");
        return getAllCountries().stream()
                .sorted(Comparator.comparingInt(Country::getPopulation).reversed())
                .toList();
    }

    /**
     * Get a country in a region where it borders the highest number of countries outside its own region.
     * For example, find which country in asia has the most borders with countries that are not in asia.
     * @param region the region the country belongs to
     * @return a {@link Country} with highest number of borders not in their region
     */
    public Country getCountryWithMostBordersOutsideRegion(String region) {
        log.info("Getting country with most borders outside its region {}", region);
        // 1. Get all countries in the specified region
        List<Country> countriesInRegion = getCountriesInRegion(region);
        log.debug("There are {} countries in {}", countriesInRegion.size(), region);

        // 2. Collect all unique border codes around the countries
        Set<String> allBorderCodes = countriesInRegion.stream()
                .filter(country -> country.getBorders() != null)
                .flatMap(country -> country.getBorders().stream())
                .collect(Collectors.toSet());

        // 3. Fetch details of all bordering countries that are not in the given region
        List<Country> borderingCountriesOutsideRegion = getCountriesByCodes(String.join(",", allBorderCodes)).stream()
                .filter(country -> !region.equalsIgnoreCase(country.getRegion()))
                .toList();
        log.debug(
                "There are {} countries outside {} bordering countries in {}",
                borderingCountriesOutsideRegion.size(),
                region,
                region);

        // 4. Find the country with the highest count of borders outside the region
        return countriesInRegion.stream()
                .max(Comparator.comparingInt(
                        country -> countBordersOutsideRegion(country, borderingCountriesOutsideRegion)))
                .orElseThrow(
                        () -> new NoSuchElementException("No country found with most borders outside the region."));
    }

    private int countBordersOutsideRegion(Country country, List<Country> allBorderingCountries) {
        if (country.getBorders() == null) {
            return 0;
        }
        return (int) country.getBorders().stream()
                .filter(Objects::nonNull)
                .filter(borderCode -> isCountryOutsideRegion(borderCode, allBorderingCountries))
                .count();
    }

    private boolean isCountryOutsideRegion(String borderCode, List<Country> allBorderingCountries) {
        return allBorderingCountries.stream()
                .anyMatch(country -> country.getBorders().stream().anyMatch(borderCode::equalsIgnoreCase));
    }
}
