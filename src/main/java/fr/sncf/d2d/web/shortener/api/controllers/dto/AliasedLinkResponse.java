package fr.sncf.d2d.web.shortener.api.controllers.dto;

import fr.sncf.d2d.web.shortener.domain.model.AliasedLink;

public record AliasedLinkResponse(
        String id,
        String shortUrl,
        String removalToken
) {

    public static AliasedLinkResponse from(AliasedLink aliasedLink) {
        return new AliasedLinkResponse(aliasedLink.id(), aliasedLink.alias(), aliasedLink.token());
    }
}
