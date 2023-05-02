package com.example.courseapi.repository;

import com.example.courseapi.domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Submission.SubmissionId>,
        JpaSpecificationExecutor<Submission> {
    void deleteBySubmissionId_LessonIdAndSubmissionId_StudentId(final Long lessonId, final Long studentId);
    Optional<Submission> findBySubmissionId_LessonIdAndSubmissionId_StudentId(final Long lessonId, final Long studentId);
    List<Submission> findAllByStudentId(final Long studentId);
    List<Submission> findAllByLessonId(final Long lessonId);
    List<Submission> findAllByStudentIdAndLessonCourseId(final Long studentId, final Long courseId);
}
