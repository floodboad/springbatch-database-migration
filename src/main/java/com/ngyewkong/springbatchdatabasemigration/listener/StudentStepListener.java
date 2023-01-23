package com.ngyewkong.springbatchdatabasemigration.listener;

import com.ngyewkong.springbatchdatabasemigration.entity.postgresql.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

@Component
@Slf4j
public class StudentStepListener implements SkipListener<Student, com.ngyewkong.springbatchdatabasemigration.entity.mysql.Student> {

    @Override
    public void onSkipInRead(Throwable throwable) {
        log.info("error during read " + throwable.getLocalizedMessage());
    }

    @Override
    public void onSkipInWrite(com.ngyewkong.springbatchdatabasemigration.entity.mysql.Student student, Throwable throwable) {
        log.debug("error during write " + student.toString());
    }

    @Override
    public void onSkipInProcess(Student student, Throwable throwable) {
        log.debug("error during processing " + student.toString());
    }

    // file writer helper
    // create a file at the filePath and allows it to be appendable (multiple records can be written)
    public void createFile(String filePath, String data) {
        try (FileWriter fileWriter = new FileWriter(new File(filePath), true)) {
            fileWriter.write(data + " -> error occurred on " + new Date() + "\n");
        } catch (Exception e) {
            log.info(e.getLocalizedMessage());
        }
    }
}
