package fr.sncf.d2d.web.shortener.domain;

import java.time.temporal.TemporalAmount;
import java.util.Optional;
import java.util.UUID;

public interface AliasedLinkRepository {

    Optional<AliasedLink> retrieve(UUID id);

    Optional<AliasedLink> retrieveByAlias(String shortValue);

    void touch(UUID id);

    AliasedLink save(AliasedLinkCreation creation);

    void remove(AliasedLink aliasedLink);

    int removeOlderThan(TemporalAmount interval);
}
