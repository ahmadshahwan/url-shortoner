package fr.sncf.d2d.web.shortener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class UrlShortenerConfiguration {

    @Bean
    public Clock getClock() {
        return Clock.systemDefaultZone();
    }
}
