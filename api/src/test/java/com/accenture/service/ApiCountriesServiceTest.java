package com.accenture.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.accenture.client.ApiRestClient;
import com.accenture.dto.Country;
import com.accenture.dto.Name;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ApiCountriesServiceTest {

    @Autowired
    private ApiCountriesService service;

    @MockBean
    private ApiRestClient mockApiRestClient;

    @Test
    void whenGetAllCountries_thenReturnListOfCountries1() {
        List<Country> expectedCountries = Arrays.asList(
                createCountry("China", "CHN", "Asia", 1_400_000_000),
                createCountry("Germany", "DEU", "Europe", 83_000_000));
        when(mockApiRestClient.executeGet(eq("/all"), eq(Country[].class), any()))
                .thenReturn(Optional.of(expectedCountries.toArray(new Country[0])));

        List<Country> actualCountries = service.getAllCountries();

        assertEquals(expectedCountries, actualCountries);
    }

    @Test
    void whenGetCountriesInRegion_thenReturnCountriesInRegion() {
        List<Country> expectedCountries = Arrays.asList(
                createCountry("China", "CHN", "Asia", 1_400_000_000),
                createCountry("India", "IND", "Asia", 1_400_000_000));
        when(mockApiRestClient.executeGet(eq("/region/asia"), eq(Country[].class), any()))
                .thenReturn(Optional.of(expectedCountries.toArray(new Country[0])));

        List<Country> actualCountries = service.getCountriesInRegion("asia");

        assertEquals(actualCountries, expectedCountries);
    }

    @Test
    void whenGetCountriesByCodes_thenReturnCountriesForCodes() {
        List<Country> expectedCountries = Arrays.asList(
                createCountry("Germany", "DEU", "Europe", 83_000_000),
                createCountry("France", "FRA", "Europe", 67_000_000));
        when(mockApiRestClient.executeGet(eq("/alpha"), eq(Country[].class), eq(Map.of("codes", "DEU,FRA"))))
                .thenReturn(Optional.of(expectedCountries.toArray(new Country[0])));

        List<Country> actualCountries = service.getCountriesByCodes("DEU,FRA");

        assertEquals(expectedCountries, actualCountries);
    }

    @Test
    void whenGetCountriesSortedByPopulation_thenCorrectlySorted() {
        List<Country> unsortedCountries = Arrays.asList(
                createCountry("Sudan", "SDN", "africa", 36_000_000),
                createCountry("India", "IND", "Asia", 1_200_000_000),
                createCountry("China", "CHN", "Asia", 1_400_000_000),
                createCountry("Wakanda", "WKN", "africa", 5_000_000));
        when(mockApiRestClient.executeGet(eq("/all"), eq(Country[].class), any()))
                .thenReturn(Optional.of(unsortedCountries.toArray(new Country[0])));

        List<Country> sortedCountries = service.getCountriesSortedByPopulation();

        assertThat(
                "First country has higher population than second country",
                sortedCountries.get(0).getPopulation() > sortedCountries.get(1).getPopulation());
        assertThat(
                "First country is China",
                "China".equalsIgnoreCase(sortedCountries.get(0).getName().getCommon()));
    }

    @Test
    void whenGetCountryWithMostBordersOutsideRegion_thenReturnCorrectCountry() {
        Country turkey = createCountry("Turkey", "TUR", "Asia", 1, "GRE", "BUL", "ARM");
        Country china = createCountry("China", "CHN", "Asia", 1, "RUS");
        Country russia = createCountry("Russia", "RUS", "Europe", 1);
        Country greece = createCountry("Greece", "GRE", "Europe", 1);
        Country bulgaria = createCountry("Bulgaria", "BUL", "Europe", 1);
        Country armenia = createCountry("Armenia", "ARM", "Asia", 1);

        when(mockApiRestClient.executeGet(eq("/region/asia"), eq(Country[].class), any()))
                .thenReturn(Optional.of(Arrays.asList(turkey, china).toArray(new Country[0])));

        when(mockApiRestClient.executeGet(eq("/alpha"), eq(Country[].class), eq(Map.of("codes", "GRE,BUL,ARM,RUS"))))
                .thenReturn(Optional.of(
                        Arrays.asList(greece, bulgaria, armenia, russia).toArray(new Country[0])));

        Country resultCountry = service.getCountryWithMostBordersOutsideRegion("asia");

        assertNotNull(resultCountry);
        assertEquals("Turkey", resultCountry.getName().getCommon());
    }

    // Helper method to create mock Country objects
    private Country createCountry(String commonName, String cioc, String region, int population, String... borders) {
        Country country = new Country();
        Name name = new Name();
        name.setCommon(commonName);
        country.setName(name);
        country.setCioc(cioc);
        country.setRegion(region);
        country.setPopulation(population);
        country.setBorders(new HashSet<>(Arrays.asList(borders)));
        return country;
    }
}
