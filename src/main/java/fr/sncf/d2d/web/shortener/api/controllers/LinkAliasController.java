package fr.sncf.d2d.web.shortener.api.controllers;

import fr.sncf.d2d.web.shortener.api.controllers.dto.AliasedLinkResponse;
import fr.sncf.d2d.web.shortener.domain.AliasedLink;
import fr.sncf.d2d.web.shortener.domain.AliasedLinkService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.util.UUID;

@RestController
@RequestMapping("links")
public class LinkAliasController {

    private final AliasedLinkService aliasedLinkService;

    private static final String REMOVAL_TOKEN_HEADER_KEY = "X-Removal-Token";

    public LinkAliasController(AliasedLinkService aliasedLinkService) {
        this.aliasedLinkService = aliasedLinkService;
    }

    @PostMapping
    public AliasedLinkResponse create(
            @RequestBody
            URL originalUrl,
            HttpServletResponse response
    ) {
        AliasedLink aliasedLink = this.aliasedLinkService.createAliasedLink(originalUrl);
        response.addHeader(REMOVAL_TOKEN_HEADER_KEY, aliasedLink.token());
        return AliasedLinkResponse.from(aliasedLink);
    }

    @DeleteMapping("{id}")
    public void delete(
            @PathVariable
            UUID id,
            @RequestHeader(value = REMOVAL_TOKEN_HEADER_KEY, required = false)
            String token
    ) {
        this.aliasedLinkService.revoke(id, token);
    }
}