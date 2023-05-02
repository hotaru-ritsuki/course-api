package com.example.courseapi.repository;

import com.example.courseapi.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long>, JpaSpecificationExecutor<Lesson> {
    List<Lesson> findByCourseId(final Long courseId);
    List<Lesson> findByTitleContaining(final String text);
    List<Lesson> findByDescriptionContaining(final String text);
}
