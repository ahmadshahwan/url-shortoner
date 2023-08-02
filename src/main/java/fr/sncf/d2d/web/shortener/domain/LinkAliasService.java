package fr.sncf.d2d.web.shortener.domain;

import org.springframework.stereotype.Service;

import java.net.URL;
import java.security.SecureRandom;
import java.time.Period;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class LinkAliasService {

    private final LinkAliasRepository repository;
    private final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public LinkAliasService(
        LinkAliasRepository repository
    ) {
        this.repository = repository;
    }

    public LinkAlias create(URL originalUrl) {
        String shortUrl = this.generateAlias();
        String token = this.generateToken();
        return this.repository.save(new LinkAliasCreation(shortUrl, originalUrl, token));
    }

    public void revoke(UUID id, Optional<String> token) {
        LinkAlias link = this.repository.retrieve(id)
                .orElseThrow(() -> LinkAliasNotFoundException.of(id));
        if (!token.map(link.token()::equals).orElse(false)) {
            throw InvalidRemovalTokenException.of(token.orElse("<empty>"));
        }
        this.repository.remove(link);
    }

    public URL resolve(String alias) {
        LinkAlias entity = this.repository.retrieveByAlias(alias)
                .orElseThrow(() -> LinkAliasNotFoundException.of(alias));
        this.repository.touch(entity.id());
        return entity.url();
    }

    public void prune() {
        this.repository.removeOlderThan(Period.ofDays(30));
    }

    private String generateAlias() {
        return this.generateRandom(6);
    }

    private String generateToken() {
        return this.generateRandom(30);
    }

    private String generateRandom(int bytesSize) {
        byte[] bytes = new byte[bytesSize];
        new SecureRandom().nextBytes(bytes);
        return this.base64Encoder.encodeToString(bytes);
    }
}
