package fr.sncf.d2d.web.shortener.domain;

import java.util.UUID;

public class LinkAliasNotFoundException extends RuntimeException {

    public LinkAliasNotFoundException(String message) {
        super(message);
    }

    public static LinkAliasNotFoundException of(String url) {
        return new LinkAliasNotFoundException("URL alias %s not found".formatted(url));
    }

    public static LinkAliasNotFoundException of(UUID id) {
        return new LinkAliasNotFoundException("URL alias with ID %s not found".formatted(id));
    }
}
