package no.nav.pam.feed.taskscheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FeedTaskService {

    private FeedTaskRepository repository;

    @Autowired
    public FeedTaskService(FeedTaskRepository repository) {
        this.repository = repository;
    }

    /**
     * Return DateTime for last run or an empty Optional
     */
    @Transactional(readOnly = true)
    public Optional<LocalDateTime> fetchLastRunDateForJob(String jobName){
        Optional<FeedTask> historyOptional = repository.findByFeedName(jobName);

        return historyOptional.map(x -> x.getLastRunDate());
    }

    @Transactional(readOnly = true)
    public List<FeedTask> fetchAllFeedTasks(){
        List<FeedTask> feedTasks = new ArrayList<>();

        repository.findAll().forEach(ft -> feedTasks.add(ft));

        return feedTasks;
    }

    @Transactional
    public void save(String jobName, LocalDateTime lastRunDate){
        Optional<FeedTask> historyOptional = repository.findByFeedName(jobName);

        FeedTask history = historyOptional.orElse(new FeedTask(jobName));
        history.setLastRunDate(lastRunDate);

        repository.save(history);
    }

}
