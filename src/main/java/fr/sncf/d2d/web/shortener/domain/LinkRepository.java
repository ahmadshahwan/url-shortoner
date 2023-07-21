package fr.sncf.d2d.web.shortener.domain;

import fr.sncf.d2d.web.shortener.domain.model.AliasedLink;

public interface LinkRepository {

    AliasedLink retrieveByShortUrl(String shortValue);

    AliasedLink save(AliasedLink aliasedLink);

    boolean deleteByToken(String token);
}
