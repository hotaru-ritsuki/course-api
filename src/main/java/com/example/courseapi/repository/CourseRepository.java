package com.example.courseapi.repository;

import com.example.courseapi.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    List<Course> findByStudentsId(Long studentId);

    List<Course> findByTitleContaining(String title);

    List<Course> findByDescriptionContaining(String description);

    boolean existsByIdAndStudentsId(Long courseId, Long studentId);

    boolean existsByIdAndInstructorsId(Long courseId, Long studentId);

    boolean existsByLessonsIdAndStudentsId(Long lessonId, Long studentId);


    boolean existsByLessonsIdAndInstructorsId(Long lessonId, Long studentId);
}
