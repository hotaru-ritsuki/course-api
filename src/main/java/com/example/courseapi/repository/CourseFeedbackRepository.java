package com.example.courseapi.repository;

import com.example.courseapi.domain.CourseFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseFeedbackRepository extends JpaRepository<CourseFeedback, Long>, JpaSpecificationExecutor<CourseFeedback> {
    List<CourseFeedback> findByStudentId(final Long studentId);
    List<CourseFeedback> findByCourseId(final Long courseId);
    List<CourseFeedback> findByFeedbackContaining(final String text);
}
