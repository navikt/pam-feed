package no.nav.pam.feed.rest;


import no.nav.pam.feed.taskscheduler.FeedTask;
import no.nav.pam.feed.taskscheduler.FeedTaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

        List<FeedTask> feedTaskLis = new ArrayList();
        feedTaskLis.add(new FeedTask("TEST1", LocalDateTime.now()));
        feedTaskLis.add(new FeedTask("TEST2", LocalDateTime.now()));

        when(feedTaskService.fetchAllFeedTasks()).thenReturn(feedTaskLis);

        ResponseEntity<List<FeedTask>> entity = controller.fetchAllFeedTasks();

        assertEquals(2, entity.getBody().size());
    }


}
