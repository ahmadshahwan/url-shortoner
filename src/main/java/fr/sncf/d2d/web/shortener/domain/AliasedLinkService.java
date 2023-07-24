package fr.sncf.d2d.web.shortener.domain;

import org.springframework.stereotype.Service;

import java.net.URL;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class AliasedLinkService {

    private final AliasedLinkRepository repository;
    private final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public AliasedLinkService(
        AliasedLinkRepository repository
    ) {
        this.repository = repository;
    }

    public AliasedLink createAliasedLink(URL originalUrl) {
        String shortUrl = this.generateAlias();
        String token = this.generateToken();
        return this.repository.save(new AliasedLinkCreation(shortUrl, originalUrl, token));
    }

    public void revoke(UUID id, Optional<String> token) {
        AliasedLink link = this.repository.retrieve(id)
                .orElseThrow(() -> AliasedLinkNotFoundException.of(id));
        if (!token.map(link.token()::equals).orElse(false)) {
            throw InvalidLinkTokenException.of(token.orElse("<empty>"));
        }
        this.repository.remove(link);
    }

    public URL resolve(String alias) {
        AliasedLink entity = this.repository.retrieveByAlias(alias)
                .orElseThrow(() -> AliasedLinkNotFoundException.of(alias));
        return entity.url();
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
