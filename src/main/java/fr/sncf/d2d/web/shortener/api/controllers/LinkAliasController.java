package fr.sncf.d2d.web.shortener.api.controllers;

import fr.sncf.d2d.web.shortener.api.controllers.dto.AliasedLinkResponse;
import fr.sncf.d2d.web.shortener.domain.AliasedLink;
import fr.sncf.d2d.web.shortener.domain.AliasedLinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("links")
public class LinkAliasController {

    private final AliasedLinkService aliasedLinkService;

    private static final String REMOVAL_TOKEN_HEADER_KEY = "X-Removal-Token";

    private static final List<String> ACCEPTED_PROTOCOLS = List.of("http", "https");

    public LinkAliasController(AliasedLinkService aliasedLinkService) {
        this.aliasedLinkService = aliasedLinkService;
    }

    @PostMapping
    public AliasedLinkResponse create(
            @RequestBody
            URL originalUrl,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        this.validateURL(originalUrl, request);
        AliasedLink aliasedLink = this.aliasedLinkService.createAliasedLink(originalUrl);
        response.addHeader(REMOVAL_TOKEN_HEADER_KEY, aliasedLink.token());
        return AliasedLinkResponse.from(aliasedLink);
    }

    @DeleteMapping("{id}")
    public void delete(
            @PathVariable
            UUID id,
            @RequestHeader(value = REMOVAL_TOKEN_HEADER_KEY)
            Optional<String> token
    ) {
        this.aliasedLinkService.revoke(id, token);
    }

    private void validateURL(URL url, HttpServletRequest request) {
        if (!ACCEPTED_PROTOCOLS.contains(url.getProtocol())) {
            throw new InvalidUrlException();
        }
        int urlPort = url.getPort() == -1 ? url.getDefaultPort() : url.getPort();
        boolean sameHost = url.getHost().equals(request.getServerName());
        boolean samePort = urlPort == request.getServerPort();
        if (sameHost && samePort) {
            throw new InvalidUrlException();
        }
    }

    @ExceptionHandler({
            InvalidUrlException.class,
            HttpMessageNotReadableException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleErrors() {
        return "Invalid URL";
    }

    static class InvalidUrlException extends RuntimeException {}
}