package org.eshishkin.edu.cometwatcher;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;
import javax.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eshishkin.edu.cometwatcher.service.CometService;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ScheduledNotifier {

    private final CometService cometService;

    @Scheduled(cron="{application.schedulers.comet-notifier}")
    public void notify(ScheduledExecution execution) {
        log.info("Comets found {}", cometService.getComets().size());
    }
}
