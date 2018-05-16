package no.nav.pam.feed.rest;


import no.nav.pam.feed.taskscheduler.FeedTaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeedTaskControllerTest {

    @Mock
    FeedTaskService feedTaskService;

    FeedTaskController controller;

    String JOB_NAME = "TEST";

    @Before
    public void init() {
        controller = new FeedTaskController(feedTaskService);
    }

    @Test
    public void should_fetch_last_run_date() {
        LocalDateTime time = LocalDateTime.now();

        when(feedTaskService.fetchLastRunDateForJob(JOB_NAME)).thenReturn(Optional.of(time));

        ResponseEntity<LocalDateTime> entity = controller.fetchLastRunDate(JOB_NAME);

        assertEquals(time, entity.getBody());
    }


}
