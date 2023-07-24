package fr.sncf.d2d.web.shortener.domain;

import java.net.URL;
import java.util.UUID;

public record AliasedLink(
        UUID id,
        String alias,
        URL url,
        String token

) {}
