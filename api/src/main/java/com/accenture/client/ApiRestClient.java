package com.accenture.client;

import static com.accenture.config.AppConfig.FIVE_MINUTES_EVICTING_CACHE_MANAGER;

import com.accenture.exception.ApiException;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiRestClient {

    private static final String BASE_URL = "https://restcountries.com/v3.1";
    private final RestTemplate restTemplate;

    /**
     * A generic way to execute GET requests for an endpoint based on the pre-defined BASE_URL via rest template
     * @param endpoint the endpoint to call
     * @param responseType the response object type
     * @param queryParams query params
     * @return an {@link Optional} of response of the given responseType
     * @param <T> generic type
     */
    @Cacheable(
            value = "apiRestCache",
            key = "{#endpoint, #queryParams}",
            cacheManager = FIVE_MINUTES_EVICTING_CACHE_MANAGER)
    public <T> Optional<T> executeGet(String endpoint, Class<T> responseType, Map<String, String> queryParams) {
        String fullUrl = BASE_URL + endpoint;
        try {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(fullUrl);
            if (queryParams != null && !queryParams.isEmpty()) {
                queryParams.forEach(uriBuilder::queryParam);
            }
            URI uri = uriBuilder.build().toUri();

            ResponseEntity<T> response = restTemplate.getForEntity(uri, responseType);
            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException e) {
            log.error("An error occurred while requesting {}", fullUrl);
            throw new ApiException(
                    e.getMessage(), HttpStatus.valueOf(e.getStatusCode().value()));
        }
    }
}
