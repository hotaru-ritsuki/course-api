package com.example.courseapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Entity class for Homework
 */
@Entity
@Table(name = "homeworks", schema = "course_management")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
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
    private Student student;
}
