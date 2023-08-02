package fr.sncf.d2d.web.shortener.spi;

import fr.sncf.d2d.web.shortener.domain.LinkAlias;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;

public class LinkAliasEntity {
    private final UUID id;
    private final String alias;
    private final URL url;
    private final String token;
    private LocalDateTime lastAccessed;

    public LinkAliasEntity(UUID id, String alias, URL url, String token, LocalDateTime lastAccessed) {
        this.id = id;
        this.alias = alias;
        this.url = url;
        this.token = token;
        this.lastAccessed = lastAccessed;
    }

    public UUID getId() {
        return id;
    }

    public String getAlias() {
        return alias;
    }

    public URL getUrl() {
        return url;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(LocalDateTime lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    public LinkAlias toLinkAlias() {
        return new LinkAlias(
                this.id,
                this.alias,
                this.url,
                this.token
        );
    }
}
