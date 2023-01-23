package com.ngyewkong.springbatchdatabasemigration.listener;

import com.ngyewkong.springbatchdatabasemigration.entity.postgresql.Subject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SubjectStepListener implements SkipListener<Subject, com.ngyewkong.springbatchdatabasemigration.entity.mysql.Subject> {

    @Override
    public void onSkipInRead(Throwable throwable) {
        log.info("error during read " + throwable.getLocalizedMessage());
    }

    @Override
    public void onSkipInWrite(com.ngyewkong.springbatchdatabasemigration.entity.mysql.Subject subject, Throwable throwable) {
        log.debug("error during write " + subject.toString());
    }

    @Override
    public void onSkipInProcess(Subject subject, Throwable throwable) {
        log.debug("error during processing " + subject.toString());
    }
}
