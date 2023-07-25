package fr.sncf.d2d.web.shortener;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.Clock;

@TestConfiguration
public class UrlShortenerTestConfiguration {

    @Bean
    public Clock getClock() {
        return Clock.systemDefaultZone();
    }
}