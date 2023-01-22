package com.ngyewkong.springbatchdatabasemigration.entity.postgresql;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

// entity class for spring data jpa
// use @Entity annotation
// Student Entity represent the student table in postgresql
// @Table annotation
// using @Data for lombok for getter and setter
@Data
@Entity
@Table(name = "student")
public class Student {
    // using @Id annotation for primary key/unique identifier
    // using @Column annotation for remaining column, name field need to match what is in the db table col
    // need to setup entity manager factory to assign the correct datasource

    @Id
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    // for field that has the same name as the db is okay to not use @Column
    private String email;

    @Column(name = "dept_id")
    private Long deptId;

    @Column(name = "is_active")
    private String isActive;
}
