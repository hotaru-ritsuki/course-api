package com.example.courseapi.service.impl;

import com.example.courseapi.domain.*;
import com.example.courseapi.dto.SubmissionDTO;
import com.example.courseapi.exception.*;
import com.example.courseapi.repository.*;
import com.example.courseapi.service.SubmissionService;
import com.example.courseapi.service.mapper.SubmissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final SubmissionMapper submissionMapper;
    private final UserRepository userRepository;

    public Optional<SubmissionDTO> findById(Long lessonId, Long studentId) {
        return submissionRepository.findBySubmissionId_LessonIdAndSubmissionId_StudentId(lessonId, studentId)
                .map(submissionMapper::toDto);
    }

    @Override
    public SubmissionDTO save(SubmissionDTO submissionDTO, Long instructorId) {
        validateSubmission(submissionDTO.getLessonId(), submissionDTO.getStudentId(), instructorId);
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

    @Override
    public SubmissionDTO saveGrade(Long lessonId, Long studentId, Double grade, Long instructorId) {
        SubmissionDTO submissionDTO = new SubmissionDTO(grade, lessonId, studentId);
        return this.save(submissionDTO, instructorId);
    }

    @Override
    public List<SubmissionDTO> findAllByLesson(Long lessonId, Long currentUserId) {
        User user = userRepository.findById(currentUserId).orElseThrow(UserNotFoundException::new);
        if (user instanceof Admin) {
            return submissionMapper.toDto(submissionRepository.findAllByLessonId(lessonId));
        } else if (user instanceof Instructor instructor) {
            return submissionRepository.findAllByLessonId(lessonId)
                    .stream()
                    .filter(submission -> submission.getLesson().getCourse().getInstructors().contains(instructor))
                    .map(submissionMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalRoleAccessException();
        }
    }

    @Override
    public List<SubmissionDTO> findAllByStudent(Long studentId, Long currentUserId) {
        User user = userRepository.findById(currentUserId).orElseThrow(UserNotFoundException::new);
        if (user instanceof Admin) {
            return submissionMapper.toDto(submissionRepository.findAllByStudentId(studentId));
        } else if (user instanceof Instructor instructor) {
            return submissionRepository.findAllByStudentId(studentId)
                    .stream()
                    .filter(submission -> submission.getLesson().getCourse().getInstructors().contains(instructor))
                    .map(submissionMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalRoleAccessException();
        }
    }

    public void validateSubmission(Long lessonId, Long studentId, Long instructorId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(LessonNotFoundException::new);
        Student student = studentRepository.findById(studentId).orElseThrow(StudentNotFoundException::new);
        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(InstructorNotFoundException::new);

        if (!lesson.getCourse().getInstructors().contains(instructor)) {
            throw new InstructorAccessException();
        }

        if (!lesson.getCourse().getStudents().contains(student)) {
            throw new StudentNotSubscribedToCourse();
        }
    }
}
