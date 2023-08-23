package fr.sncf.d2d.web.shortener.spi;

import fr.sncf.d2d.web.shortener.domain.LinkAlias;
import fr.sncf.d2d.web.shortener.domain.LinkAliasCreation;
import fr.sncf.d2d.web.shortener.domain.LinkAliasRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

public class InMemoryLinkAliasRepository implements LinkAliasRepository {

    protected final Map<UUID, LinkAliasEntity> data = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, LinkAliasEntity> aliasIndex = Collections.synchronizedMap(new HashMap<>());
    private final SortedMap<LocalDateTime, LinkAliasEntity> lastAccessedIndex =
            Collections.synchronizedSortedMap(new TreeMap<>());

    private final Clock clock;

    public InMemoryLinkAliasRepository(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Optional<LinkAlias> retrieve(UUID id) {
        LinkAliasEntity link = this.data.get(id);
        if (link == null) {
            return Optional.empty();
        }
        return Optional.of(link).map(LinkAliasEntity::toLinkAlias);
    }

    @Override
    public Optional<LinkAlias> retrieveByAlias(String shortValue) {
        return Optional.ofNullable(this.aliasIndex.get(shortValue))
                .map(LinkAliasEntity::toLinkAlias);
    }

    @Override
    public void touch(UUID id) {
        LinkAliasEntity link = this.data.get(id);
        this.lastAccessedIndex.remove(link.getLastAccessed(), link);
        link.setLastAccessed(LocalDateTime.now(this.clock));
        this.lastAccessedIndex.put(link.getLastAccessed(), link);
    }

    @Override
    public LinkAlias save(LinkAliasCreation creation) {
        LinkAliasEntity entity = new LinkAliasEntity(
                UUID.randomUUID(),
                creation.alias(),
                creation.url(),
                creation.token(),
                LocalDateTime.now(this.clock)
        );
        this.add(entity);
        return entity.toLinkAlias();
    }

    @Override
    public void remove(LinkAlias linkAlias) {
        Objects.requireNonNull(linkAlias, "Removed aliased link cannot be null.");
        LinkAliasEntity entity = this.data.get(linkAlias.id());
        if (entity == null) {
            throw new IllegalArgumentException("No entity for link alias %s.".formatted(linkAlias));
        }
        this.remove(entity);
    }

    @Override
    public int removeOlderThan(TemporalAmount interval) {
        LocalDateTime threshold = LocalDateTime.now(this.clock).minus(interval);
        Map<LocalDateTime, LinkAliasEntity> toRemove =
                this.lastAccessedIndex.headMap(threshold);
        int size = toRemove.size();
        toRemove.values().forEach(link -> {
            this.data.remove(link.getId());
            this.aliasIndex.remove(link.getAlias());
        });
        toRemove.clear();
        return size;
    }

    protected void add(LinkAliasEntity entity) {
        this.data.put(entity.getId(), entity);
        this.aliasIndex.put(entity.getAlias(), entity);
        this.lastAccessedIndex.put(entity.getLastAccessed(), entity);
    }

    private void remove(LinkAliasEntity entity) {
        this.data.remove(entity.getId());
        this.aliasIndex.remove(entity.getAlias());
        this.lastAccessedIndex.remove(entity.getLastAccessed());
    }
}
