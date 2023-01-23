package com.ngyewkong.springbatchdatabasemigration.processor;

import com.ngyewkong.springbatchdatabasemigration.entity.postgresql.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StudentItemProcessor implements ItemProcessor<Student, com.ngyewkong.springbatchdatabasemigration.entity.mysql.Student> {

    @Override
    public com.ngyewkong.springbatchdatabasemigration.entity.mysql.Student process(Student postgresStudent) throws Exception {
        com.ngyewkong.springbatchdatabasemigration.entity.mysql.Student mysqlStudent = new com.ngyewkong.springbatchdatabasemigration.entity.mysql.Student();

        // set mysql student using postgres student
        mysqlStudent.setId(postgresStudent.getId());
        mysqlStudent.setFirstName(postgresStudent.getFirstName());
        mysqlStudent.setLastName(postgresStudent.getLastName());
        mysqlStudent.setEmail(postgresStudent.getEmail());
        mysqlStudent.setDeptId(postgresStudent.getDeptId());

        // rmb data type mismatch of isActive in postgres and mysql
        mysqlStudent.setIsActive(postgresStudent.getIsActive() != null && Boolean.parseBoolean(postgresStudent.getIsActive()));

        log.info("Processing from postgresql to mysql " + mysqlStudent.toString());

        return mysqlStudent;
    }
}
