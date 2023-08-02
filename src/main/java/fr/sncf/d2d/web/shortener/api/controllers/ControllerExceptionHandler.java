package fr.sncf.d2d.web.shortener.api.controllers;

import fr.sncf.d2d.web.shortener.domain.LinkAliasNotFoundException;
import fr.sncf.d2d.web.shortener.domain.InvalidRemovalTokenException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = Logger.getLogger(ControllerExceptionHandler.class.getName());

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handle(LinkAliasNotFoundException ignore) {
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handle(InvalidRemovalTokenException ignore) {
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(MethodArgumentTypeMismatchException ignore) {
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handle(Throwable e, HttpServletRequest request) {
        String message = "%s %s  from %s, %s: %s (%s line %s)".formatted(
                request == null ? "NONE" : request.getMethod(),
                request == null ? "NONE" : request.getRequestURI(),
                request == null ? "NONE" : request.getRemoteAddr(),
                e.getClass().getName(),
                e.getMessage(),
                e.getStackTrace().length == 0 ? "UNKNOWN" : e.getStackTrace()[0].getFileName(),
                e.getStackTrace().length == 0 ? "UNKNOWN" : e.getStackTrace()[0].getLineNumber()
        );
        this.logger.log(Level.SEVERE, message);
    }
}
