package com.example.courseapi.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "homeworks", schema = "course_management")
@Data
@EqualsAndHashCode(callSuper = true)
public class Homework extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homework_id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "title")
    private String title;

    @Column(name = "file_path")
    private String filePath;

    @ManyToOne
    private Lesson lesson;

    @ManyToOne
    private User student;
}
