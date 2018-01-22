package no.nav.pam.feed.taskscheduler;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "FEEDTASK")
@SequenceGenerator(name="FEEDTASK_SEQ", sequenceName="FEEDTASK_SEQ", allocationSize=1)
public class FeedTask {

    @Id
    @GeneratedValue(generator = "FEEDTASK_SEQ")
    private Long id;

    @Column(name = "FEED_NAME")
    private String feedName;

    @Column(name="LAST_RUN_DATE")
    private Long lastRunDate;

    FeedTask() {
    }

    public FeedTask(String feedName) {
        this.feedName = feedName;
    }

    public FeedTask(String feedName, Long lastRunDate) {
        this.feedName = feedName;
        this.lastRunDate = lastRunDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public Long getLastRunDate() {
        return lastRunDate;
    }

    public void setLastRunDate(Long lastRunDate) {
        this.lastRunDate = lastRunDate;
    }
}
