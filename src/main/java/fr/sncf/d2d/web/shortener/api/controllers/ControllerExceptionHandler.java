package fr.sncf.d2d.web.shortener.api.controllers;

import fr.sncf.d2d.web.shortener.domain.AliasedLinkNotFoundException;
import fr.sncf.d2d.web.shortener.domain.InvalidLinkTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handle(AliasedLinkNotFoundException ignore) {
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public void handle(InvalidLinkTokenException ignore) {
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void handle(MethodArgumentTypeMismatchException ignore) {
    }
}
