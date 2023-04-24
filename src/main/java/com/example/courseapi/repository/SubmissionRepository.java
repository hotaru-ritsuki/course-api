package com.example.courseapi.repository;

import com.example.courseapi.domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Submission.SubmissionId> {

    void deleteBySubmissionId_LessonIdAndSubmissionId_StudentId(Long lessonId, Long studentId);

    Optional<Submission> findBySubmissionId_LessonIdAndSubmissionId_StudentId(Long lessonId, Long studentId);

    List<Submission> findAllByStudentId(Long studentId);
    List<Submission> findAllByLessonId(Long lessonId);

    List<Submission> findAllByStudentIdAndLessonCourseId(Long studentId, Long courseId);
}
