package fr.sncf.d2d.web.shortener.domain;

import java.util.Optional;
import java.util.UUID;

public interface AliasedLinkRepository {

    Optional<AliasedLink> retrieve(UUID id);

    Optional<AliasedLink> retrieveByAlias(String shortValue);

    AliasedLink save(AliasedLinkCreation creation);

    void remove(AliasedLink aliasedLink);
}
