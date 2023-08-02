package fr.sncf.d2d.web.shortener.domain;

public class InvalidRemovalTokenException extends RuntimeException {

    public InvalidRemovalTokenException(String message) {
        super(message);
    }

    public static InvalidRemovalTokenException of(String token) {
        return new InvalidRemovalTokenException("Invalid token %s not found".formatted(token));
    }
}
