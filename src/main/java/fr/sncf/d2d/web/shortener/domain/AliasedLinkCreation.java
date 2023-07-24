package fr.sncf.d2d.web.shortener.domain;

import java.net.URL;

public record AliasedLinkCreation(
        String alias,
        URL url,
        String token

) {}
