//package com.test.shedlocktest.components;
//
//import com.test.shedlocktest.services.MailboxService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import net.javacrumbs.shedlock.core.LockProvider;
//import net.javacrumbs.shedlock.core.SimpleLock;
//import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class BaeldungTaskScheduler {
//
//    @Autowired
//    private LockProvider lockProvider;
//
//    private final MailboxService mailboxService;
//    private final int runForInSecondes = 3600;
//    @Value("${SPRING_JOB_NAMES}:jobName")
//    private String jobName;
//
//    //    Try to run job toutes les 18 secondes
//    @Scheduled(cron = "0/" + (runForInSecondes / 200) + " * * * * ?")
////    @Scheduled(fixedDelayString = "PT" + runForInSecondes + "S")-
//    @SchedulerLock(name = "BaeldungTaskScheduler", lockAtLeastFor = "PT" + (runForInSecondes / 200) + "S", lockAtMostFor = "PT" + (runForInSecondes + 5) + "S")
//    public void scheduledTask() {
//        log.info("BaeldungTaskScheduler.scheduledTask() '{}' on '{}' started and will be lockAtLeastFor '{}'s, atMostFor '{}'s, runFor '{}'s, every '{}'s", Thread.currentThread().getName(), this.jobName, (runForInSecondes / 200), (runForInSecondes + 5), (runForInSecondes / 199), (runForInSecondes / 200));
//        log.info("Launch mailboxService.run()");
//        mailboxService.run(runForInSecondes / 199);
//
//    }
//
//}
