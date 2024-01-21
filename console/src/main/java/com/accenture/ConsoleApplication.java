package com.accenture;

import com.accenture.dto.Country;
import com.accenture.service.ConsoleCountriesService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsoleApplication {
    public static void main(String[] args) {
        ConsoleCountriesService countriesService = new ConsoleCountriesService();

        log.info("===========================================================================");
        Country asian = countriesService.getCountryWithMostBordersOutsideRegion("asia");
        log.info(
                "Asian country with most borders with non asian country: {}",
                asian.getName().getOfficial());
        log.info("===========================================================================\n");

        log.info("===========================================================================");
        List<Country> countries = countriesService.getCountriesSortedByPopulation();
        log.info("All countries sorted by population in descending order");
        for (Country country : countries) {
            log.info("Country: {}\tPopulation: {}", country.getName().getOfficial(), country.getPopulation());
        }
        log.info("===========================================================================");
    }
}
