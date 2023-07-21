package fr.sncf.d2d.web.shortener.spi;

import fr.sncf.d2d.web.shortener.domain.AliasedLinkRepository;
import fr.sncf.d2d.web.shortener.domain.AliasedLink;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class InMemoryAliasedLinkRepository implements AliasedLinkRepository {

    private final Map<UUID, AliasedLink> data = new HashMap<>();
    private final Map<String, AliasedLink> aliasIndex = new HashMap<>();

    @Override
    public AliasedLink retrieve(UUID id) {
        return this.data.get(id);
    }

    @Override
    public AliasedLink retrieveByAlias(String shortValue) {
        return this.aliasIndex.get(shortValue);
    }

    @Override
    public AliasedLink save(AliasedLink aliasedLink) {
        UUID id = UUID.randomUUID();
        AliasedLink newAliasedLink = aliasedLink.withId(id);
        this.data.put(id, newAliasedLink);
        this.aliasIndex.put(aliasedLink.alias(), newAliasedLink);
        return newAliasedLink;
    }

    @Override
    public void remove(AliasedLink aliasedLink) {
        if (aliasedLink == null) {
            throw new IllegalArgumentException("Removed aliased link cannot be null.");
        }
        this.data.remove(aliasedLink.id());
        this.aliasIndex.remove(aliasedLink.alias());
    }
}
