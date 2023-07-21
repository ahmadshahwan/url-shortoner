package fr.sncf.d2d.web.shortener.domain;

import fr.sncf.d2d.web.shortener.domain.model.AliasedLink;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

@Service
public class LinkService {

    private final LinkRepository linkRepository;
    private final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public LinkService(
        LinkRepository linkRepository
    ) {
        this.linkRepository = linkRepository;
    }

    public AliasedLink createAliasedLink(URL originalUrl) {
        String shortUrl = this.generateAlias();
        String token = this.generateToken();
        return this.linkRepository.save(new AliasedLink(shortUrl, originalUrl, token));
    }

    public void revoke(String token) {
        if (!this.linkRepository.deleteByToken(token)) {
            throw LinkTokenNotFoundException.of(token);
        }
    }

    public URL resolve(String alias) {
        AliasedLink entity = this.linkRepository.retrieveByShortUrl(alias);
        if (entity == null) {
            throw LinkAliasNotFoundException.of(alias);
        }
        return entity.url();
    }

    private String generateAlias() {
        byte[] bytes = new byte[3];
        new Random().nextBytes(bytes);
        return this.base64Encoder.encodeToString(bytes);
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
