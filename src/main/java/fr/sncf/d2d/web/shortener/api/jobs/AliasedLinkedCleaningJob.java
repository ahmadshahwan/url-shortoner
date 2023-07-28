package fr.sncf.d2d.web.shortener.api.jobs;

import fr.sncf.d2d.web.shortener.domain.AliasedLinkService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AliasedLinkedCleaningJob {

    private final AliasedLinkService aliasedLinkService;

    public AliasedLinkedCleaningJob(
            AliasedLinkService aliasedLinkService
    ) {
        this.aliasedLinkService = aliasedLinkService;
    }

    @Scheduled(cron = "0 0 04 * * *")
    public void cleanOldAliasedLinks() {
        this.aliasedLinkService.prune();
    }
}
