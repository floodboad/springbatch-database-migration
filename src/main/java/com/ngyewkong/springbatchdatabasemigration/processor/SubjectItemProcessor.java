package com.ngyewkong.springbatchdatabasemigration.processor;

import com.ngyewkong.springbatchdatabasemigration.entity.postgresql.Subject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SubjectItemProcessor implements ItemProcessor<Subject, com.ngyewkong.springbatchdatabasemigration.entity.mysql.Subject> {
    @Override
    public com.ngyewkong.springbatchdatabasemigration.entity.mysql.Subject process(Subject postgresSubject) throws Exception {
        com.ngyewkong.springbatchdatabasemigration.entity.mysql.Subject mysqlSubject = new com.ngyewkong.springbatchdatabasemigration.entity.mysql.Subject();
        mysqlSubject.setId(postgresSubject.getId());
        mysqlSubject.setSubjectName(postgresSubject.getSubjectName());
        mysqlSubject.setStudentId(postgresSubject.getStudentId());
        mysqlSubject.setMark(postgresSubject.getMark());

        log.info("Processing from postgresql to mysql " + mysqlSubject.toString());

        return mysqlSubject;
    }
}
