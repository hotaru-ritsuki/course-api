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
import com.example.courseapi.exception.*;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.repository.*;
import com.example.courseapi.service.CourseService;
import com.example.courseapi.service.SubmissionService;
import com.example.courseapi.service.mapper.CourseMapper;
import com.example.courseapi.service.mapper.LessonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Log4j2
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
    public Optional<CourseResponseDTO> findById(final Long courseId) {
        log.debug("Finding course by id: {}", courseId);
        return courseRepository.findById(courseId).map(courseMapper::toResponseDto);
    }

    @Override
    @Transactional
    public CourseResponseDTO save(final CourseRequestDTO courseDTO) {
        log.debug("Saving course : {}", courseDTO);
        validateCourseInstructors(courseDTO);
        Course course = courseRepository.save(courseMapper.fromRequestDto(courseDTO));
        return courseMapper.toResponseDto(course);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseResponseDTO> findAll(final Filters filters, final Pageable pageable, final User user) {
        log.debug("Finding all courses by filters and pageable");
        if (user instanceof Student) {
            log.debug("Current user is student. Only available courses will be shown");
            filters.include(new FilterImpl("available", SpecificationComparison.EQUALS, true));
        }
        return courseRepository.findAll(new SpecificationBuilder<Course>(filters).build(), pageable)
                .map(courseMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponseDTO> findByStudentId(final Long studentId) {
        log.debug("Finding all courses by student id: {}", studentId);
        return courseMapper.toResponseDto(courseRepository.findByStudentsId(studentId));
    }

    @Override
    @Transactional
    public void delete(final Long courseId) {
        log.debug("Deleting course with id: {}", courseId);
        Course course = courseRepository.findById(courseId).orElseThrow(() ->
                new SystemException("Course with id: " + courseId + " not found.", ErrorCode.NOT_FOUND));
        courseRepository.delete(course);
    }

    @Override
    @Transactional
    public void subscribeStudentToCourse(final Long courseId, final Long studentId) {
        log.debug("Subscribing student with id: {} to course with id: {}", studentId, courseId);
        Course targetCourse = courseRepository.findById(courseId).orElseThrow(() ->
                new SystemException("Course with id: " + courseId + "not found", ErrorCode.BAD_REQUEST));
        Student student = studentRepository.findById(studentId).orElseThrow(() ->
                new SystemException("Student with id: " + studentId + "not found", ErrorCode.BAD_REQUEST));
        if (!targetCourse.getAvailable()) {
            throw new SystemException("Course with id: " + courseId + " is not available for registration.",
                    ErrorCode.FORBIDDEN);
        }

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
    public CourseStatusResponseDTO getCourseStatus(final Long courseId, final Long studentId) {
        log.debug("Getting course status for course with id: {} and student with id: {}",
                courseId, studentId);
        if (!isStudentSubscribedToCourse(courseId, studentId)) {
            throw new SystemException("Student with id: " + studentId +
                    " is not subscribed to course with id: " + courseId, ErrorCode.BAD_REQUEST);
        }

        Course course = courseRepository.findById(courseId).orElseThrow(() ->
                new SystemException("Course with id: " + courseId + "not found", ErrorCode.BAD_REQUEST));
        CourseGradeDTO courseGradeDTO = calculateCourseStatus(studentId, course);
        return courseMapper.toResponseStatusDto(course, studentId, courseGradeDTO);
    }

    @Override
    public CourseGradeDTO calculateCourseStatus(final Long studentId, final Course course) {
        log.debug("Calculating course grade status for course with id: {} and student with id: {}",
                course.getId(), studentId);
        CourseGradeDTO courseGradeDTO = new CourseGradeDTO();
        List<Submission> studentCourseSubmissions = submissionService.findAllByStudentIdAndCourseId(studentId, course.getId());

        int lessonsInCourse = course.getLessons().size();
        log.debug("For course with id: {} found {} lesson(s)", course.getId(), lessonsInCourse);
        log.debug("For course with id: {} and student with id: {} found {} submission(s)",
                course.getId(), studentId, studentCourseSubmissions.size());
        if (lessonsInCourse != studentCourseSubmissions.size()) {
            log.debug("Course Status: In Progress! Course id: {}, Student id: {}",
                    course.getId(), studentId);
            courseGradeDTO.setCourseStatus(CourseStatus.IN_PROGRESS);
        } else {
            double sumOfGrades = studentCourseSubmissions.stream().mapToDouble(Submission::getGrade).sum();
            double finalGrade = sumOfGrades / (double) lessonsInCourse;
            if (lessonsInCourse != 0) {
                courseGradeDTO.setFinalGrade(finalGrade);
            }
            if (finalGrade >= 80.0) {
                log.debug("Course Status: Completed Successfully! Final grade: {}\n" +
                                " Course id: {}, Student id: {}", finalGrade, course.getId(), studentId);
                courseGradeDTO.setCourseStatus(CourseStatus.COMPLETED);
            } else {
                log.debug("Course Status: Failed! Grade level wasn't passed. Final grade: {} \n" +
                                " Course id: {}, Student id: {}", finalGrade, course.getId(), studentId);
                courseGradeDTO.setCourseStatus(CourseStatus.FAILED);
            }
        }
        return courseGradeDTO;
    }

    @Override
    public Set<CourseGradeDTO> calculateCourseStatus(final Long studentId, final Set<Course> courses) {
        log.debug("Calculating course grade statuses for courses with ids: {} and student with id: {}",
                courses.stream().map(Course::getId).toList(), studentId);
        return CollectionUtils.isEmpty(courses) ?
                Collections.emptySet() :
                courses.stream()
                        .map(course -> calculateCourseStatus(studentId, course))
                        .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isStudentSubscribedToCourse(final Long courseId, final Long studentId) {
        log.debug("Checking if student with id: {} is already subscribed to course with id: {}",
                studentId, courseId);
        return courseRepository.existsByIdAndStudentsId(courseId, studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<? extends CourseResponseDTO> getMyCourses(final Long userId) {
        log.debug("Getting my courses for user: {}", userId);
        User currentUser = userRepository.findById(userId).orElseThrow(() ->
                new SystemException("User with id: " + userId + " not found.", ErrorCode.BAD_REQUEST));
        if (currentUser.getRole().equals(Roles.STUDENT) && currentUser instanceof Student currentStudent) {
            log.debug("Current user is student. Getting student courses for user: {}", userId);
            return currentStudent.getStudentCourses().stream()
                    .map(course -> {
                        CourseGradeDTO courseGradeDTO = calculateCourseStatus(currentStudent.getId(), course);
                        return courseMapper.toResponseStatusDto(course, currentStudent.getId(), courseGradeDTO);
                    })
                    .collect(Collectors.toSet());
        } else if (currentUser.getRole().equals(Roles.INSTRUCTOR) && currentUser instanceof Instructor currentInstructor) {
            log.debug("Current user is instructor. Getting instructor courses for user: {}", userId);
            return courseMapper.toResponseDto(currentInstructor.getInstructorCourses());
        } else {
            throw new SystemException("Illegal role access", ErrorCode.FORBIDDEN);
        }
    }

    @Override
    @Transactional
    public CourseResponseDTO addInstructorToCourse(final Long courseId, final Long instructorId) {
        log.debug("Adding instructor with id: {} to course with id: {}", instructorId, courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new SystemException("Course with id: " + courseId + " not found",
                        ErrorCode.BAD_REQUEST));
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new SystemException("Instructor with id: " + instructorId + " not found",
                        ErrorCode.BAD_REQUEST));
        course.addInstructor(instructor);
        course = courseRepository.save(course);
        return courseMapper.toResponseDto(course);
    }

    @Override
    @Transactional
    public CourseResponseDTO deleteInstructorForCourse(final Long courseId, final Long instructorId) {
        log.debug("Deleting instructor with id: {} to course with id: {}", instructorId, courseId);
        Course course = courseRepository.findById(courseId).orElseThrow(() ->
                new SystemException("Course with id: " + courseId + " not found", ErrorCode.BAD_REQUEST));
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new SystemException("Instructor with id: " + instructorId + "not found",
                        ErrorCode.BAD_REQUEST));
        course.removeInstructor(instructor);
        course = courseRepository.save(course);
        return courseMapper.toResponseDto(course);
    }

    @Override
    @Transactional
    public CourseResponseDTO updateCourseLessonsAndSave(final Long courseId, final LessonsUpdateDTO lessonsDTO) {
        log.debug("Updating course with id: {} with lessons: {}", courseId, lessonsDTO);
        Course course = courseRepository.findById(courseId).orElseThrow(() ->
                new SystemException("Course with id: " + courseId + " not found", ErrorCode.BAD_REQUEST));

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

    private void validateCourseInstructors(final CourseRequestDTO courseDTO) {
        log.debug("Validating course instructors for course : {}", courseDTO);
        if (Objects.nonNull(courseDTO.getInstructorIds())) {
            List<Long> missingInstructors = courseDTO.getInstructorIds().stream()
                    .filter(Predicate.not(instructorRepository::existsById))
                    .toList();
            if (CollectionUtils.isNotEmpty(missingInstructors)) {
                throw new SystemException("Instructors with id(s): " + missingInstructors + " not found",
                        ErrorCode.BAD_REQUEST);
            }
        }
        log.debug("Validation processed successfully for course : {}", courseDTO);
    }
}
