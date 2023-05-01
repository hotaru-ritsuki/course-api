package com.example.courseapi.service.impl;

import com.example.courseapi.domain.*;
import com.example.courseapi.dto.request.SubmissionRequestDTO;
import com.example.courseapi.dto.response.SubmissionResponseDTO;
import com.example.courseapi.exception.*;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.repository.*;
import com.example.courseapi.service.SubmissionService;
import com.example.courseapi.service.mapper.SubmissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final SubmissionMapper submissionMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<SubmissionResponseDTO> findById(Long lessonId, Long studentId) {
        return submissionRepository.findBySubmissionId_LessonIdAndSubmissionId_StudentId(lessonId, studentId)
                .map(submissionMapper::toResponseDto);
    }

    @Override
    @Transactional
    public SubmissionResponseDTO save(SubmissionRequestDTO submissionRequestDTO) {
        validateSubmission(submissionRequestDTO.getLessonId(), submissionRequestDTO.getStudentId());
        Submission submission = submissionMapper.fromRequestDto(submissionRequestDTO);
        submission = submissionRepository.save(submission);

        return submissionMapper.toResponseDto(submission);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubmissionResponseDTO> findAll() {
        return submissionMapper.toResponseDto(submissionRepository.findAll());
    }

    @Override
    @Transactional
    public void delete(Long lessonId, Long studentId) {
        submissionRepository.deleteBySubmissionId_LessonIdAndSubmissionId_StudentId(lessonId, studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Submission> findAllByStudentIdAndCourseId(Long studentId, Long courseId) {
        return submissionRepository.findAllByStudentIdAndLessonCourseId(studentId, courseId);
    }

    @Override
    @Transactional
    public SubmissionResponseDTO saveGrade(Long lessonId, Long studentId, Double grade) {
        SubmissionRequestDTO submissionRequestDTO = new SubmissionRequestDTO(grade, lessonId, studentId);
        return this.save(submissionRequestDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubmissionResponseDTO> findAllByLesson(Long lessonId, Long currentUserId) {
        return submissionMapper.toResponseDto(submissionRepository.findAllByLessonId(lessonId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubmissionResponseDTO> findAllByStudent(Long studentId, Long currentUserId) {
        User user = userRepository.findById(currentUserId).orElseThrow(() ->
                new SystemException("User with id: " + currentUserId + " not found.", ErrorCode.BAD_REQUEST));
        if (user instanceof Admin || (user instanceof Student student && currentUserId.equals(studentId))) {
            return submissionMapper.toResponseDto(submissionRepository.findAllByStudentId(studentId));
        } else if (user instanceof Instructor instructor) {
            return submissionRepository.findAllByStudentId(studentId)
                    .stream()
                    .filter(submission -> submission.getLesson().getCourse().getInstructors().contains(instructor))
                    .map(submissionMapper::toResponseDto).collect(Collectors.toList());
        }
        throw new SystemException("Illegal role access", ErrorCode.FORBIDDEN);
    }

    @Transactional(readOnly = true)
    public void validateSubmission(Long lessonId, Long studentId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() ->
                new SystemException("Lesson with id: " + lessonId + " not found.", ErrorCode.BAD_REQUEST));
        Student student = studentRepository.findById(studentId).orElseThrow(() ->
                new SystemException("Student with id: " + studentId + " not found.", ErrorCode.BAD_REQUEST));

        if (!lesson.getCourse().getStudents().contains(student)) {
            throw new SystemException("Student with id: " + studentId +
                    " is not subscribed to course with id: " + lesson.getCourse().getId(), ErrorCode.BAD_REQUEST);
        }
    }
}
