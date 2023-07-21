package fr.sncf.d2d.web.shortener.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class InvalidLinkTokenException extends RuntimeException {

    public InvalidLinkTokenException(String message) {
        super(message);
    }

    public static InvalidLinkTokenException of(String token) {
        return new InvalidLinkTokenException("Invalid token %s not found".formatted(token));
    }
}
