package fr.sncf.d2d.web.shortener.domain;

import java.util.UUID;

public interface AliasedLinkRepository {

    AliasedLink retrieve(UUID id);

    AliasedLink retrieveByAlias(String shortValue);

    AliasedLink save(AliasedLink aliasedLink);

    void remove(AliasedLink aliasedLink);
}
