package fr.sncf.d2d.web.shortener.api.controllers;

import fr.sncf.d2d.web.shortener.domain.AliasedLink;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(LinkAliasResolver.class)
class LinkAliasResolverTest extends ControllerTest {

    @Test
    void should_resolve_existing_alias() throws Exception {
        AliasedLink link = givenAliasedLink();
        RequestBuilder request = MockMvcRequestBuilders
                .get("/%s".formatted(link.alias()));
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, URL));
    }
}