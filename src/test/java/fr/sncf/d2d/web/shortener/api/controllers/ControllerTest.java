package fr.sncf.d2d.web.shortener.api.controllers;

import fr.sncf.d2d.web.shortener.UrlShortenerTestConfiguration;
import fr.sncf.d2d.web.shortener.domain.AliasedLink;
import fr.sncf.d2d.web.shortener.domain.AliasedLinkService;
import fr.sncf.d2d.web.shortener.spi.InMemoryAliasedLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.net.MalformedURLException;
import java.net.URL;

@Import(UrlShortenerTestConfiguration.class)
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mvc;

    @SpyBean
    protected InMemoryAliasedLinkRepository aliasedLinkRepository;

    @SpyBean
    protected AliasedLinkService aliasedLinkService;

    protected static final String URL = "https://www.sncf.fr";

    protected AliasedLink givenAliasedLink() throws MalformedURLException {
        return aliasedLinkService.createAliasedLink(new URL(URL));
    }
}
