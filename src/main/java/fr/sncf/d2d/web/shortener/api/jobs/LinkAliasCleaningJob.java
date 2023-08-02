package fr.sncf.d2d.web.shortener.api.jobs;

import fr.sncf.d2d.web.shortener.domain.LinkAliasService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LinkAliasCleaningJob {

    private final LinkAliasService linkAliasService;

    public LinkAliasCleaningJob(
            LinkAliasService linkAliasService
    ) {
        this.linkAliasService = linkAliasService;
    }

    @Scheduled(cron = "0 0 04 * * *")
    public void cleanOldLinkAliases() {
        this.linkAliasService.prune();
    }
}
