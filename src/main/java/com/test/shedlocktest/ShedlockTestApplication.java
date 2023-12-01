package com.test.shedlocktest;

import com.test.shedlocktest.services.JobService;
import com.test.shedlocktest.services.MailboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
public class ShedlockTestApplication  {

    private final JobService jobService;
    private final MailboxService mailboxService;

    public static void main(String[] args) {
        SpringApplication.run(ShedlockTestApplication.class, args);
    }

//    @Override
//    public void run(String... args) {
//        jobService.run();
//        mailboxService.run();
//    }

}
