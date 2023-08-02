package fr.sncf.d2d.web.shortener.api.controllers.dto;

import fr.sncf.d2d.web.shortener.domain.LinkAlias;

import java.net.URL;
import java.util.UUID;

public record LinkAliasResponse(
        UUID id,
        String shortId,
        URL realUrl
) {

    public static LinkAliasResponse from(LinkAlias linkAlias) {
        return new LinkAliasResponse(linkAlias.id(), linkAlias.alias(), linkAlias.url());
    }
}
