package fr.sncf.d2d.web.shortener.api.controllers;

import fr.sncf.d2d.web.shortener.domain.LinkAlias;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@WebMvcTest(LinkAliasController.class)
class LinkAliasControllerTest extends ControllerTest {

    @Test
    void post_should_create_valid_link() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/links")
                .content("\"%s\"".formatted(URL))
                .contentType(MediaType.APPLICATION_JSON);
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.real-url").value(URL))
                .andExpect(MockMvcResultMatchers.jsonPath("$.short-id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }

    @Test
    void delete_should_answer_ok_when_token_is_correct() throws Exception {
        LinkAlias link = givenLinkAlias();
        RequestBuilder request = MockMvcRequestBuilders
                .delete("/links/%s".formatted(link.id()))
                .header("X-Removal-Token", link.token());
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void delete_should_answer_403_when_token_is_incorrect() throws Exception {
        LinkAlias link = givenLinkAlias();
        RequestBuilder request = MockMvcRequestBuilders
                .delete("/links/%s".formatted(link.id()))
                .header("X-Removal-Token", "not a token");
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void delete_should_answer_403_when_token_is_absent() throws Exception {
        LinkAlias link = givenLinkAlias();
        RequestBuilder request = MockMvcRequestBuilders
                .delete("/links/%s".formatted(link.id()));
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void delete_unrecognized_id_should_answer_not_found() throws Exception {
        LinkAlias link = givenLinkAlias();
        RequestBuilder request = MockMvcRequestBuilders
                .delete("/links/%s".formatted(UUID.randomUUID().toString()));
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void delete_twice_should_answer_not_found() throws Exception {
        LinkAlias link = givenLinkAlias();
        RequestBuilder request = MockMvcRequestBuilders
                .delete("/links/%s".formatted(link.id()))
                .header("X-Removal-Token", link.token());
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk());
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}