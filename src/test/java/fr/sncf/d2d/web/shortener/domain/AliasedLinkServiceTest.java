package fr.sncf.d2d.web.shortener.domain;

import fr.sncf.d2d.web.shortener.spi.InMemoryAliasedLinkRepository;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Clock;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AliasedLinkServiceTest {

    private final InMemoryAliasedLinkRepository repository = new InMemoryAliasedLinkRepository(Clock.systemDefaultZone());
    private final AliasedLinkService sut = new AliasedLinkService(repository);

    @Test
    void create_aliased_link() throws MalformedURLException {
        URL url = new URL("https://www.sncf.fr");
        var link = this.sut.createAliasedLink(url);
        assertEquals(link.url(), url);
        assertNotNull(link.id());
        assertNotNull(link.token());
    }

    @Test
    void revoke() throws MalformedURLException {
        URL url = new URL("https://www.sncf.fr");
        var link = this.sut.createAliasedLink(url);
        this.sut.revoke(link.id(), Optional.of(link.token()));
        assertThrows(AliasedLinkNotFoundException.class, () -> this.sut.resolve(link.alias()));
    }

    @Test
    void resolve() throws MalformedURLException {
        var link = this.sut.createAliasedLink(new URL("https://www.sncf.fr"));
        URL url = this.sut.resolve(link.alias());
        assertEquals(url, link.url());
    }
}