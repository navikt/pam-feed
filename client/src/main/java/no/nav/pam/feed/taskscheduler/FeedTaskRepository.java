package no.nav.pam.feed.taskscheduler;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedTaskRepository extends CrudRepository<FeedTask, Long> {

    Optional<FeedTask> findByFeedName(String feeName);
}
