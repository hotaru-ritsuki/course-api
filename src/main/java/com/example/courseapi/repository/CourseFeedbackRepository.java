package com.example.courseapi.repository;

import com.example.courseapi.domain.CourseFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseFeedbackRepository extends JpaRepository<CourseFeedback, Long> {
    List<CourseFeedback> findByStudentId(Long studentId);

    List<CourseFeedback> findByCourseId(Long courseId);

    List<CourseFeedback> findByFeedbackContaining(String text);
}
