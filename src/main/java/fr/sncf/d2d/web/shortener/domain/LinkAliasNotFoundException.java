package fr.sncf.d2d.web.shortener.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class LinkAliasNotFoundException extends RuntimeException {

    public LinkAliasNotFoundException(String message) {
        super(message);
    }

    public static LinkAliasNotFoundException of(String url) {
        return new LinkAliasNotFoundException("URL alias %s not found".formatted(url));
    }
}
