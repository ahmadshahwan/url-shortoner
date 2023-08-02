package fr.sncf.d2d.web.shortener.domain;

import java.net.URL;

public record LinkAliasCreation(
        String alias,
        URL url,
        String token

) {}
