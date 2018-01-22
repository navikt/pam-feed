package no.nav.pam.feed.taskscheduler;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.ScheduledLockConfiguration;
import net.javacrumbs.shedlock.spring.ScheduledLockConfigurationBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.time.Duration;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Bean
    public ScheduledLockConfiguration taskScheduler(LockProvider lockProvider, @Value("${feed.poolsize:10}")
    int poolSize, @Value("${feed.duration:10L}") long duration) {
        return ScheduledLockConfigurationBuilder
                .withLockProvider(lockProvider)
                .withPoolSize(poolSize)
                .withDefaultLockAtMostFor(Duration.ofMinutes(duration))
                .build();
    }

    @Bean
    public LockProvider lockProvider (DataSource dataSource) {
        return new JdbcTemplateLockProvider(dataSource);
    }

}
