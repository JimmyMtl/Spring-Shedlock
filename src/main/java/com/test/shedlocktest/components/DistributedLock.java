package com.test.shedlocktest.components;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Inherited
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(java.lang.annotation.ElementType.METHOD)
@SchedulerLock(name = "DistributedLock")
public @interface DistributedLock {
    @AliasFor(annotation = SchedulerLock.class) String name();
}
