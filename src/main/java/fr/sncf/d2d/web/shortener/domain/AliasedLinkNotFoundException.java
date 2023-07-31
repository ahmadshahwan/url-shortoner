package fr.sncf.d2d.web.shortener.domain;

import java.util.UUID;

public class AliasedLinkNotFoundException extends RuntimeException {

    public AliasedLinkNotFoundException(String message) {
        super(message);
    }

    public static AliasedLinkNotFoundException of(String url) {
        return new AliasedLinkNotFoundException("URL alias %s not found".formatted(url));
    }

    public static AliasedLinkNotFoundException of(UUID id) {
        return new AliasedLinkNotFoundException("URL alias with ID %s not found".formatted(id));
    }
}
