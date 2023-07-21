package fr.sncf.d2d.web.shortener.domain;

import java.net.URL;
import java.util.UUID;

public record AliasedLink(
        UUID id,
        String alias,
        URL url,
        String token

) {
    public AliasedLink(
            String shortValue,
            URL originalValue,
            String token

    ) {
        this(null, shortValue, originalValue, token);
    }

    public AliasedLink withId(UUID id) {
        return new AliasedLink(
                id,
                this.alias(),
                this.url(),
                this.token()
        );
    }
}
