package fr.sncf.d2d.web.shortener.api.controllers;

import fr.sncf.d2d.web.shortener.domain.LinkAliasService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URL;

@Controller
@RequestMapping
public class LinkAliasResolver {

    private final LinkAliasService linkAliasService;

    LinkAliasResolver(LinkAliasService linkAliasService) {
        this.linkAliasService = linkAliasService;
    }

    @GetMapping("/{url}")
    public RedirectView resolve(
            @PathVariable
            String url
    ) {
        URL originalUrl = this.linkAliasService.resolve(url);
        return new RedirectView(originalUrl.toString());
    }
}
