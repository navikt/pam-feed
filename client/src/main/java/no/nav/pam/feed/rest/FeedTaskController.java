package no.nav.pam.feed.rest;

import no.nav.pam.feed.taskscheduler.FeedTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/feedtask", produces = APPLICATION_JSON_VALUE)
public class FeedTaskController {

    private static final Logger LOG = LoggerFactory.getLogger(FeedTaskController.class);

    private FeedTaskService feedTaskService;

    @Autowired
    public FeedTaskController(FeedTaskService feedTaskService) {
        this.feedTaskService = feedTaskService;
    }

    @PostMapping()
    @Transactional
    public void update(@RequestParam("name") String jobName,
                       @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastRunDate) {

        feedTaskService.save(jobName, lastRunDate);
        LOG.info("Changed last run date for job {} to {}", jobName, lastRunDate);
    }
}
