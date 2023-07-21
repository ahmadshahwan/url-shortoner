package fr.sncf.d2d.web.shortener.domain.model;

import java.net.URL;

public record AliasedLink(
        String id,
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

    public AliasedLink withId(String id) {
        return new AliasedLink(
                id,
                this.alias(),
                this.url(),
                this.token()
        );
    }
}
