package com.example.courseapi.service.impl;

import com.example.courseapi.config.args.generic.FilterImpl;
import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.config.args.generic.SpecificationComparison;
import com.example.courseapi.config.args.specs.SpecificationBuilder;
import com.example.courseapi.domain.*;
import com.example.courseapi.domain.enums.CourseStatus;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.dto.*;
import com.example.courseapi.dto.request.CourseRequestDTO;
import com.example.courseapi.dto.request.LessonRequestDTO;
import com.example.courseapi.dto.request.LessonsUpdateDTO;
import com.example.courseapi.dto.response.CourseResponseDTO;
import com.example.courseapi.dto.response.CourseStatusResponseDTO;
import com.example.courseapi.dto.response.LessonResponseDTO;
import com.example.courseapi.exception.*;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.repository.*;
import com.example.courseapi.service.CourseService;
import com.example.courseapi.service.SubmissionService;
import com.example.courseapi.service.mapper.CourseMapper;
import com.example.courseapi.service.mapper.LessonMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final CourseMapper courseMapper;
    private final LessonMapper lessonMapper;
    private final SubmissionService submissionService;

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseResponseDTO> findById(Long id) {
        return courseRepository.findById(id).map(courseMapper::toResponseDto);
    }

    @Override
    @Transactional
    public CourseResponseDTO save(CourseRequestDTO courseDTO) {
        validateCourseInstructors(courseDTO);
        Course course = courseRepository.save(courseMapper.fromRequestDto(courseDTO));
        return courseMapper.toResponseDto(course);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseResponseDTO> findAll(Filters filters, Pageable pageable, User user) {
        if (user instanceof Student) {
            filters.include(new FilterImpl("available", SpecificationComparison.EQUALS, true));
        }
        return courseRepository.findAll(new SpecificationBuilder<Course>(filters).build(), pageable)
                .map(courseMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponseDTO> findByStudentId(Long studentId) {
        return courseMapper.toResponseDto(courseRepository.findByStudentsId(studentId));
    }

    @Override
    @Transactional
    public void delete(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() ->
                new SystemException("Course with id: " + courseId + " not found.", ErrorCode.NOT_FOUND));
        courseRepository.delete(course);
    }

    @Override
    @Transactional
    public void subscribeStudentToCourse(Long courseId, Long studentId) {
        Course targetCourse = courseRepository.findById(courseId).orElseThrow(() ->
                new SystemException("Course with id: " + courseId + "not found", ErrorCode.BAD_REQUEST));
        Student student = studentRepository.findById(studentId).orElseThrow(() ->
                new SystemException("Student with id: " + studentId + "not found", ErrorCode.BAD_REQUEST));

        Set<Course> studentCourses = student.getStudentCourses();

        if (studentCourses.contains(targetCourse)) {
            throw new SystemException("Student already subscribed to the course with id: " + courseId,
                    ErrorCode.BAD_REQUEST);
        }

        if (studentCourses.size() > 5) {
            throw new SystemException("Course limit for student " + student.getEmail() + " exceeded.",
                    ErrorCode.BAD_REQUEST);
        }

        targetCourse.addStudent(student);
        courseRepository.save(targetCourse);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseStatusResponseDTO getCourseStatus(Long courseId, Long studentId) {
        if (!isStudentSubscribedToCourse(courseId, studentId)) {
            throw new SystemException("Student with id: " + studentId +
                    " is not subscribed to course with id: " + courseId, ErrorCode.BAD_REQUEST);
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new SystemException("Course with id: " + courseId + "not found",
                        ErrorCode.BAD_REQUEST));
        CourseGradeDTO courseGradeDTO = calculateCourseStatus(studentId, course);
        return courseMapper.toResponseStatusDto(course, studentId, courseGradeDTO);
    }

    @Override
    public CourseGradeDTO calculateCourseStatus(Long studentId, Course course) {
        CourseGradeDTO courseGradeDTO = new CourseGradeDTO();
        List<Submission> studentCourseSubmissions = submissionService.findAllByStudentIdAndCourseId(studentId, course.getId());

        int lessonsInCourse = course.getLessons().size();

        if (lessonsInCourse != studentCourseSubmissions.size()) {
            courseGradeDTO.setCourseStatus(CourseStatus.IN_PROGRESS);
        } else {
            double sumOfGrades = studentCourseSubmissions.stream().mapToDouble(Submission::getGrade).sum();
            double finalGrade = sumOfGrades / (double) lessonsInCourse;
            if (lessonsInCourse != 0) {
                courseGradeDTO.setFinalGrade(finalGrade);
            }
            if (finalGrade >= 80.0) {
                courseGradeDTO.setCourseStatus(CourseStatus.COMPLETED);
            } else {
                courseGradeDTO.setCourseStatus(CourseStatus.FAILED);
            }
        }
        return courseGradeDTO;
    }

    @Override
    public Set<CourseGradeDTO> calculateCourseStatus(Long studentId, Set<Course> courses) {
        return CollectionUtils.isEmpty(courses) ?
                Collections.emptySet() :
                courses.stream()
                        .map(course -> calculateCourseStatus(studentId, course))
                        .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isStudentSubscribedToCourse(Long courseId, Long studentId) {
        return courseRepository.existsByIdAndStudentsId(courseId, studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<? extends CourseResponseDTO> getMyCourses(Long userId) {
        User currentUser = userRepository.findById(userId).orElseThrow(() ->
                new SystemException("User with id: " + userId + " not found.", ErrorCode.BAD_REQUEST));
        if (currentUser.getRole().equals(Roles.STUDENT) && currentUser instanceof Student currentStudent) {
            return currentStudent.getStudentCourses().stream()
                    .map(course -> {
                        CourseGradeDTO courseGradeDTO = calculateCourseStatus(currentStudent.getId(), course);
                        return courseMapper.toResponseStatusDto(course, currentStudent.getId(), courseGradeDTO);
                    })
                    .collect(Collectors.toSet());
        } else if (currentUser.getRole().equals(Roles.INSTRUCTOR) && currentUser instanceof Instructor currentInstructor) {
            return courseMapper.toResponseDto(currentInstructor.getInstructorCourses());
        } else {
            throw new SystemException("Illegal role access", ErrorCode.FORBIDDEN);
        }
    }

    @Override
    @Transactional
    public CourseResponseDTO addInstructorToCourse(Long courseId, Long instructorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new SystemException("Course with id: " + courseId + "not found",
                        ErrorCode.BAD_REQUEST));
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new SystemException("Instructor with id: " + instructorId + "not found",
                        ErrorCode.BAD_REQUEST));
        course.addInstructor(instructor);
        course = courseRepository.save(course);
        return courseMapper.toResponseDto(course);
    }

    @Override
    @Transactional
    public CourseResponseDTO deleteInstructorForCourse(Long courseId, Long instructorId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() ->
                new SystemException("Course with id: " + courseId + "not found", ErrorCode.BAD_REQUEST));
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new SystemException("Instructor with id: " + instructorId + "not found",
                        ErrorCode.BAD_REQUEST));
        course.removeInstructor(instructor);
        course = courseRepository.save(course);
        return courseMapper.toResponseDto(course);
    }

    @Override
    @Transactional
    public CourseResponseDTO updateCourseLessons(Long courseId, LessonsUpdateDTO lessonsDTO) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new SystemException("Course with id: " + courseId + "not found",
                ErrorCode.BAD_REQUEST));

        course.getLessons().clear();
        // Update the lessons in the Course entity
        for (LessonRequestDTO lessonRequestDTO : lessonsDTO.getLessons()) {
            if (lessonRequestDTO.getId() == null) {
                // This is a new lesson, so add it to the set of lessons
                lessonRequestDTO.setCourseId(course.getId());
                Lesson savedLesson = lessonRepository.save(lessonMapper.fromRequestDto(lessonRequestDTO));
                course.addLesson(savedLesson);
            } else {
                // This is an existing lesson, so update its properties
                Lesson existingLesson = lessonRepository.findById(lessonRequestDTO.getId()).orElseThrow(() ->
                        new SystemException("Lesson with id: " + lessonRequestDTO.getId() + " not found.", ErrorCode.BAD_REQUEST));
                if (!existingLesson.getCourse().getId().equals(courseId) && existingLesson.getCourse().getLessons().size() <=5) {
                    throw new SystemException("Attempt to reassign lesson to another course will break course with id: " + existingLesson.getCourse().getId(),
                            ErrorCode.BAD_REQUEST);
                }
                existingLesson.setTitle(lessonRequestDTO.getTitle());
                existingLesson.setDescription(lessonRequestDTO.getDescription());
                lessonRepository.save(existingLesson);
                course.addLesson(existingLesson);
            }
        }


        // Save the updated Course entity
        return courseMapper.toResponseDto(courseRepository.save(course));
    }

    private void validateCourseInstructors(CourseRequestDTO courseDTO) {
        if (Objects.nonNull(courseDTO.getInstructorIds())) {
            List<Long> missingInstructors = courseDTO.getInstructorIds().stream()
                    .filter(Predicate.not(instructorRepository::existsById))
                    .toList();
            if (CollectionUtils.isNotEmpty(missingInstructors)) {
                throw new SystemException("Instructors with id(s): " + missingInstructors.toString() + " not found",
                        ErrorCode.BAD_REQUEST);
            }
        }
    }
}
