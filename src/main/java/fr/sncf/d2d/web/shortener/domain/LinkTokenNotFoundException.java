package fr.sncf.d2d.web.shortener.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class LinkTokenNotFoundException extends RuntimeException {

    public LinkTokenNotFoundException(String message) {
        super(message);
    }

    public static LinkTokenNotFoundException of(String url) {
        return new LinkTokenNotFoundException("URL token %s not found".formatted(url));
    }
}
