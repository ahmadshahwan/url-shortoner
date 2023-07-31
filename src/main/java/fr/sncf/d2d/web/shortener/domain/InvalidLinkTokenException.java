package fr.sncf.d2d.web.shortener.domain;

public class InvalidLinkTokenException extends RuntimeException {

    public InvalidLinkTokenException(String message) {
        super(message);
    }

    public static InvalidLinkTokenException of(String token) {
        return new InvalidLinkTokenException("Invalid token %s not found".formatted(token));
    }
}
