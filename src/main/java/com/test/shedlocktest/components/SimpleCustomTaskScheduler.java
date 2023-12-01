package com.test.shedlocktest.components;

import com.test.shedlocktest.services.JdbcLockSingleKeyService;
import com.test.shedlocktest.services.MailboxService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
@RequiredArgsConstructor
public class SimpleCustomTaskScheduler {

    private final MailboxService mailboxService;

    private final JdbcLockSingleKeyService jdbcLockSingleKeyService;

    @Transactional
    @EventListener(ApplicationStartedEvent.class)
    @Scheduled(cron = "*/15 * * * * ?")
    @SchedulerLock(name = "SimpleCustomTaskScheduler", lockAtLeastFor = "PT30S", lockAtMostFor = "PT7200S")
    public void run() {
        log.info("SimpleCustomTaskScheduler.run() '{}' started", Thread.currentThread().getName());
//        String createdName = jdbcLockSingleKeyService.acquire("SimpleCustomTaskScheduler", 30);
//        if (createdName == null) {
//            log.info("Lock is locked!");
//            return;
//        }

        log.info("Launch mailboxService.run()");
        try {
            mailboxService.run(15);
        } catch (Exception e) {
            log.error("Error while running mailboxService.run()", e);
        } finally {
//            this.unLock("SimpleCustomTaskScheduler");
            jdbcLockSingleKeyService.refresh("SimpleCustomTaskScheduler", 0);
        }
    }

    @PreDestroy
    public void onDestroy() {
        log.info("SimpleCustomTaskScheduler.onDestroy() '{}' started", Thread.currentThread().getName());
        jdbcLockSingleKeyService.refresh("SimpleCustomTaskScheduler", 0);
//        jdbcLockSingleKeyService.release("SimpleCustomTaskScheduler");
        log.info("SimpleCustomTaskScheduler.onDestroy() '{}' executed", Thread.currentThread().getName());
    }
//
//    public boolean tryLock(String lockname, Duration lockDuration) {
//        Duration minDuration = Duration.ZERO;
//        Instant now = ClockProvider.now();
//        LockConfiguration config = new LockConfiguration(now, lockname, lockDuration, minDuration);
//        Optional<SimpleLock> lockLocal = lockProvider.lock(config);
//        if (lockLocal.isPresent()) {
//            locks.put(lockname, lockLocal.get());
//            log.debug("lock is created!");
//            return true;
//        } else {
//            log.debug("lock is locked!");
//            return false;
//        }
//    }
//
//    public boolean extendLock(String lockname, Duration duration) {
//        Duration minDuration = Duration.ZERO;
//        SimpleLock lock = locks.get(lockname);
//        if (lock != null) {
//            Optional<SimpleLock> localLock = lock.extend(duration, duration);
//            locks.put(lockname, localLock.get());
//            log.debug("Lock is extended");
//            return true;
//        } else {
//            log.debug("There is no lock or the lock is already unlocked! Create a lock with tryLock() if you need!");
//            return false;
//        }
//
//    }
//
//    public String unLock(String lockname) {
//        log.info("SimpleCustomTaskScheduler.unLock() '{}' started", Thread.currentThread().getName());
//        SimpleLock lock = locks.get(lockname);
//        if (lock != null) {
//            log.info("Lock is unLocking!");
//            locks.remove(lockname);
//            lock.unlock();
//            log.info("Lock is unLocked!");
//            return "Lock is unLocked!";
//        } else {
//
//            log.info("There is no lock or the lock is already unlocked! Create a lock if you need!");
//            return "There is no lock or the lock is already unlocked! Create a lock if you need!";
//        }
//
//
//    }
}
