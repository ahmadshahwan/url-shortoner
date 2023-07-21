package fr.sncf.d2d.web.shortener.spi;

import fr.sncf.d2d.web.shortener.domain.LinkRepository;
import fr.sncf.d2d.web.shortener.domain.model.AliasedLink;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class InMemoryLinkRepository implements LinkRepository {

    private final Map<String, AliasedLink> data = new HashMap<>();

    @Override
    public AliasedLink retrieveByShortUrl(String shortValue) {
        return this.data.get(shortValue);
    }

    @Override
    public AliasedLink save(AliasedLink aliasedLink) {
        String id = UUID.randomUUID().toString();
        AliasedLink newAliasedLink = aliasedLink.withId(id);
        this.data.put(aliasedLink.alias(), newAliasedLink);
        return newAliasedLink;
    }

    @Override
    public boolean deleteByToken(String token) {
        String alias = this.data.entrySet().stream()
                .filter(e -> e.getValue().token().equals(token))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        return alias != null && this.data.remove(alias) != null;
    }
}
