package fr.sncf.d2d.web.shortener.spi;

import fr.sncf.d2d.web.shortener.domain.AliasedLinkCreation;
import fr.sncf.d2d.web.shortener.domain.AliasedLinkRepository;
import fr.sncf.d2d.web.shortener.domain.AliasedLink;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

@Service
public class InMemoryAliasedLinkRepository implements AliasedLinkRepository {

    private final Map<UUID, AliasedLinkEntity> data = new HashMap<>();
    private final Map<String, AliasedLinkEntity> aliasIndex = new HashMap<>();
    private final Map<LocalDateTime, AliasedLinkEntity> lastAccessedIndex = new TreeMap<>();

    private final Clock clock;

    public InMemoryAliasedLinkRepository(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Optional<AliasedLink> retrieve(UUID id) {
        AliasedLinkEntity link = this.data.get(id);
        if (link == null) {
            return Optional.empty();
        }
        return Optional.of(link).map(AliasedLinkEntity::toAliasedLink);
    }

    @Override
    public Optional<AliasedLink> retrieveByAlias(String shortValue) {
        return Optional.ofNullable(this.aliasIndex.get(shortValue))
                .map(AliasedLinkEntity::toAliasedLink);
    }

    @Override
    public AliasedLink save(AliasedLinkCreation creation) {
        AliasedLinkEntity entity = new AliasedLinkEntity(
                UUID.randomUUID(),
                creation.alias(),
                creation.url(),
                creation.token(),
                LocalDateTime.now(this.clock)
        );
        this.add(entity);
        return entity.toAliasedLink();
    }

    @Override
    public void remove(AliasedLink aliasedLink) {
        if (aliasedLink == null) {
            throw new IllegalArgumentException("Removed aliased link cannot be null.");
        }
        AliasedLinkEntity entity = this.data.get(aliasedLink.id());
        if (entity == null) {
            throw new IllegalArgumentException("No entity for link alias %s.".formatted(aliasedLink));
        }
        this.remove(entity);
    }

    @Override
    public void removeOlderThan(LocalDateTime localDateTime) {
        this.lastAccessedIndex.entrySet().stream()
                .filter(e -> e.getKey().isBefore(localDateTime))
                .map(Map.Entry::getValue)
                .forEach(this::remove);
    }

    private void add(AliasedLinkEntity entity) {
        this.data.put(entity.getId(), entity);
        this.aliasIndex.put(entity.getAlias(), entity);
        this.lastAccessedIndex.put(entity.getLastAccessed(), entity);
    }

    private void remove(AliasedLinkEntity entity) {
        this.data.remove(entity.getId());
        this.aliasIndex.remove(entity.getAlias());
        this.lastAccessedIndex.remove(entity.getLastAccessed());
    }
}
