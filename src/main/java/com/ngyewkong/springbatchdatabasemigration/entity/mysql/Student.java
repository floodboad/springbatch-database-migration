package com.ngyewkong.springbatchdatabasemigration.entity.mysql;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

// setting up the entity class for the migrated student table in mysql
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

    // same name as the mysql db table column
    private String email;

    @Column(name = "dept_id")
    private Long deptId;

    // isActive is boolean here as mysql we map it to be tinyint(1) 0 or 1
    @Column(name = "is_active")
    private Boolean isActive;
}
