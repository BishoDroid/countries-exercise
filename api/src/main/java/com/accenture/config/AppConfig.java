package com.accenture.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@EnableCaching
@Configuration
public class AppConfig {

    public static final String FIVE_MINUTES_EVICTING_CACHE_MANAGER = "fiveMinutesEvictingCacheManager";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OpenAPI openApiConfig() {

        Contact contact = new Contact();
        contact.setEmail("elbashir.adam@hotmail.com");
        contact.setName("Elbashir Adam");
        contact.setUrl("https://www.linkedin.com/in/mohamed-elbashir-adam-160303a7/");

        Info info = new Info()
                .title("Accenture Countries Exercise")
                .version("1.0")
                .contact(contact)
                .description(
                        "This API exposes endpoints view information of countries based on restcountries.com api.");

        return new OpenAPI().info(info);
    }

    @Bean(FIVE_MINUTES_EVICTING_CACHE_MANAGER)
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(
                Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).recordStats());
        return cacheManager;
    }
}
