package fr.sncf.d2d.web.shortener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.Clock;

@TestConfiguration
public class UrlShortenerTestConfiguration {

    private final UrlShortenerConfiguration config = new UrlShortenerConfiguration();

    @Bean
    public Clock getClock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public ObjectMapper getObjectMapper(Jackson2ObjectMapperBuilder builder) {
        return this.config.getObjectMapper(builder);
    }
}