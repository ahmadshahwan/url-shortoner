package fr.sncf.d2d.web.shortener.api.controllers;

import fr.sncf.d2d.web.shortener.api.controllers.dto.AliasedLinkResponse;
import fr.sncf.d2d.web.shortener.domain.LinkService;
import fr.sncf.d2d.web.shortener.domain.model.AliasedLink;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;

@RestController
@RequestMapping("links")
public class LinkAliasController {

    private final LinkService linkService;

    public LinkAliasController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping
    public AliasedLinkResponse create(
            @RequestBody
            URL originalUrl
    ) {
        AliasedLink aliasedLink = this.linkService.createAliasedLink(originalUrl);
        return AliasedLinkResponse.from(aliasedLink);
    }

    @DeleteMapping("{token}")
    public void delete(
            @PathVariable
            String token
    ) {
        this.linkService.revoke(token);
    }
}