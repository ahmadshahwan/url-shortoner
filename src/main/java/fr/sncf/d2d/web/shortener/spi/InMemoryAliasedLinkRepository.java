package fr.sncf.d2d.web.shortener.spi;

import fr.sncf.d2d.web.shortener.domain.AliasedLinkCreation;
import fr.sncf.d2d.web.shortener.domain.AliasedLinkRepository;
import fr.sncf.d2d.web.shortener.domain.AliasedLink;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class InMemoryAliasedLinkRepository implements AliasedLinkRepository {

    private final Map<UUID, AliasedLink> data = new HashMap<>();
    private final Map<String, AliasedLink> aliasIndex = new HashMap<>();

    @Override
    public Optional<AliasedLink> retrieve(UUID id) {
        return Optional.ofNullable(this.data.get(id));
    }

    @Override
    public Optional<AliasedLink> retrieveByAlias(String shortValue) {
        return Optional.ofNullable(this.aliasIndex.get(shortValue));
    }

    @Override
    public AliasedLink save(AliasedLinkCreation creation) {
        AliasedLink newAliasedLink = new AliasedLink(
                UUID.randomUUID(),
                creation.alias(),
                creation.url(),
                creation.token()
        );
        this.data.put(newAliasedLink.id(), newAliasedLink);
        this.aliasIndex.put(creation.alias(), newAliasedLink);
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
