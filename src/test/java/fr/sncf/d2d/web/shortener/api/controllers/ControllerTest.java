package fr.sncf.d2d.web.shortener.api.controllers;

import fr.sncf.d2d.web.shortener.UrlShortenerTestConfiguration;
import fr.sncf.d2d.web.shortener.domain.LinkAlias;
import fr.sncf.d2d.web.shortener.domain.LinkAliasService;
import fr.sncf.d2d.web.shortener.spi.InMemoryLinkAliasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Clock;

@Import(UrlShortenerTestConfiguration.class)
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mvc;

    @SpyBean
    protected InMemoryLinkAliasRepository linkAliasRepository;

    @SpyBean
    protected LinkAliasService linkAliasService;

    @SpyBean
    protected Clock clock;

    protected static final String URL = "https://www.sncf.fr";

    protected LinkAlias givenLinkAlias() throws MalformedURLException {
        return linkAliasService.create(new URL(URL));
    }
}
