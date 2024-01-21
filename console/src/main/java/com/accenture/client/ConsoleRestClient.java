package com.accenture.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Using enum to create a Singleton rest client.
 *
 */
@Slf4j
public enum ConsoleRestClient {
    INSTANCE;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String BASE_URL = "https://restcountries.com/v3.1";

    public <T> Optional<T> get(String endpoint, Class<T> responseType, Map<String, String> queryParams) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + endpoint).newBuilder();
        if (queryParams != null && !queryParams.isEmpty()) {
            queryParams.forEach(urlBuilder::addQueryParameter);
        }
        String fullUrl = urlBuilder.build().toString();

        log.debug("Executing GET request {}", fullUrl);
        Request request = new Request.Builder().url(fullUrl).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Error while executing request. {}", response.code());
                return Optional.empty();
            }
            return Optional.ofNullable(parseResponse(response, responseType));
        } catch (Exception e) {
            log.error("Exception occurred during request: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private <T> T parseResponse(Response response, Class<T> responseType) throws IOException {
        if (response.body() == null) {
            return null;
        }
        String responseBody = response.body().string();
        return objectMapper.readValue(responseBody, responseType);
    }
}
