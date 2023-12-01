package com.test.shedlocktest.services.impl;

import com.test.shedlocktest.services.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    public void run() {
        log.info("JobServiceImpl.run()");
    }
}
