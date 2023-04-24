package com.example.courseapi.service.impl;

import com.example.courseapi.domain.Submission;
import com.example.courseapi.dto.SubmissionDTO;
import com.example.courseapi.repository.SubmissionRepository;
import com.example.courseapi.service.SubmissionService;
import com.example.courseapi.service.mapper.SubmissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final SubmissionMapper submissionMapper;

    public Optional<SubmissionDTO> findById(Long lessonId, Long studentId) {
        return submissionRepository.findBySubmissionId_LessonIdAndSubmissionId_StudentId(lessonId, studentId)
                .map(submissionMapper::toDto);
    }

    @Override
    public SubmissionDTO save(SubmissionDTO submissionDTO) {
        Submission submission = submissionMapper.toEntity(submissionDTO);
        submission = submissionRepository.save(submission);

        return submissionMapper.toDto(submission);
    }

    @Override
    public List<SubmissionDTO> findAll() {
        return submissionMapper.toDto(submissionRepository.findAll());
    }

    @Override
    public void delete(Long lessonId, Long studentId) {
        submissionRepository.deleteBySubmissionId_LessonIdAndSubmissionId_StudentId(lessonId, studentId);
    }

    @Override
    public List<Submission> findAllByStudentIdAndCourseId(Long studentId, Long courseId) {
        return submissionRepository.findAllByStudentIdAndLessonCourseId(studentId, courseId);
    }
}
