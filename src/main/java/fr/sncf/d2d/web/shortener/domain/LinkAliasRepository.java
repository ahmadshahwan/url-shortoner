package fr.sncf.d2d.web.shortener.domain;

import java.time.temporal.TemporalAmount;
import java.util.Optional;
import java.util.UUID;

public interface LinkAliasRepository {

    Optional<LinkAlias> retrieve(UUID id);

    Optional<LinkAlias> retrieveByAlias(String shortValue);

    void touch(UUID id);

    LinkAlias save(LinkAliasCreation creation);

    void remove(LinkAlias linkAlias);

    int removeOlderThan(TemporalAmount interval);
}
