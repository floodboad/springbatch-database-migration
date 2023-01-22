package com.ngyewkong.springbatchdatabasemigration.entity.postgresql;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "subjects_learning")
public class Subject {

    @Id
    private Long id;

    @Column(name = "sub_name")
    private String subjectName;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "marks_obtained")
    private Long mark;
}
