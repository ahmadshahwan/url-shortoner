package fr.sncf.d2d.web.shortener.api.controllers.dto;

import fr.sncf.d2d.web.shortener.domain.AliasedLink;

import java.util.UUID;

public record AliasedLinkResponse(
        UUID id,
        String shortUrl
) {

    public static AliasedLinkResponse from(AliasedLink aliasedLink) {
        return new AliasedLinkResponse(aliasedLink.id(), aliasedLink.alias());
    }
}
