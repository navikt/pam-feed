package no.nav.pam.feed.taskscheduler;

import java.time.Duration;
import javax.sql.DataSource;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.ScheduledLockConfiguration;
import net.javacrumbs.shedlock.spring.ScheduledLockConfigurationBuilder;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT60S")
public class SchedulerConfig {

    @Bean
    public ScheduledLockConfiguration scheduledLockConfiguration(
        LockProvider lockProvider,
        @Value("${feed.poolsize:10}") int poolSize,
        @Value("${feed.duration:10}") long duration
    ) {

        return ScheduledLockConfigurationBuilder
            .withLockProvider(lockProvider)
            .withPoolSize(poolSize)
            .withDefaultLockAtMostFor(Duration.ofMinutes(duration))
            .build();

    }

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(dataSource);
    }

}
