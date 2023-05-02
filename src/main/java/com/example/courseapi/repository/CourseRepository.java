package com.example.courseapi.repository;

import com.example.courseapi.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    List<Course> findByStudentsId(final Long studentId);
    List<Course> findByTitleContaining(final String title);
    List<Course> findByDescriptionContaining(final String description);
    boolean existsByIdAndStudentsId(final Long courseId, final Long studentId);
    boolean existsByIdAndInstructorsId(final Long courseId, final Long studentId);
    boolean existsByLessonsIdAndStudentsId(final Long lessonId, final Long studentId);
    boolean existsByLessonsIdAndInstructorsId(final Long lessonId, final Long studentId);
}
