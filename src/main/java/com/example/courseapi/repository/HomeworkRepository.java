package com.example.courseapi.repository;

import com.example.courseapi.domain.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long>, JpaSpecificationExecutor<Homework> {
    List<Homework> findByLessonId(Long lessonId);
    List<Homework> findByStudentId(Long studentId);
    List<Homework> findByTitleContaining(String text);

    List<Homework> findByStudentIdAndLessonId(Long studentId, Long lessonId);
}
