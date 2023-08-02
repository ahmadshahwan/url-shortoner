package fr.sncf.d2d.web.shortener.spi;

import fr.sncf.d2d.web.shortener.domain.AliasedLink;
import fr.sncf.d2d.web.shortener.domain.AliasedLinkCreation;
import fr.sncf.d2d.web.shortener.domain.AliasedLinkRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

public class InMemoryAliasedLinkRepository implements AliasedLinkRepository {

    protected final Map<UUID, AliasedLinkEntity> data = new HashMap<>();
    private final Map<String, AliasedLinkEntity> aliasIndex = new HashMap<>();
    private final SortedMap<LocalDateTime, AliasedLinkEntity> lastAccessedIndex = new TreeMap<>();

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
    public void touch(UUID id) {
        AliasedLinkEntity link = this.data.get(id);
        this.lastAccessedIndex.remove(link.getLastAccessed(), link);
        if (link == null) {
            return;
        }
        link.setLastAccessed(LocalDateTime.now(this.clock));
        this.lastAccessedIndex.put(link.getLastAccessed(), link);
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
        Objects.requireNonNull(aliasedLink, "Removed aliased link cannot be null.");
        AliasedLinkEntity entity = this.data.get(aliasedLink.id());
        if (entity == null) {
            throw new IllegalArgumentException("No entity for link alias %s.".formatted(aliasedLink));
        }
        this.remove(entity);
    }

    @Override
    public int removeOlderThan(TemporalAmount interval) {
        LocalDateTime threshold = LocalDateTime.now(this.clock).minus(interval);
        Map<LocalDateTime, AliasedLinkEntity> toRemove =
                this.lastAccessedIndex.headMap(threshold);
        int size = toRemove.size();
        toRemove.values().forEach(link -> {
            this.data.remove(link.getId());
            this.aliasIndex.remove(link.getAlias());
        });
        toRemove.clear();
        return size;
    }

    protected void add(AliasedLinkEntity entity) {
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
