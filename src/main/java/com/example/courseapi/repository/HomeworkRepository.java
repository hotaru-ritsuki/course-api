package com.example.courseapi.repository;

import com.example.courseapi.domain.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    List<Homework> findByLessonId(Long lessonId);
    List<Homework> findByStudentId(Long studentId);
}
