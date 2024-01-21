package com.accenture.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A class representing a portion of the response returned from <a href="https://restcountries.com/v3.1/all">restcountries.com</a>
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {

    private Name name;
    private String cioc;
    private String cca3;
    private String region;
    private int population;
    private Set<String> borders;
}
