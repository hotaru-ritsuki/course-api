package com.example.courseapi.service.impl;

import com.example.courseapi.config.args.generic.Filters;
import com.example.courseapi.config.args.specs.SpecificationBuilder;
import com.example.courseapi.domain.*;
import com.example.courseapi.domain.enums.CourseStatus;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.dto.CourseDTO;
import com.example.courseapi.dto.CourseGradeDTO;
import com.example.courseapi.dto.CourseStatusDTO;
import com.example.courseapi.exception.*;
import com.example.courseapi.repository.CourseRepository;
import com.example.courseapi.repository.InstructorRepository;
import com.example.courseapi.repository.StudentRepository;
import com.example.courseapi.repository.UserRepository;
import com.example.courseapi.service.CourseService;
import com.example.courseapi.service.SubmissionService;
import com.example.courseapi.service.mapper.CourseMapper;
import com.example.courseapi.service.mapper.CourseStatusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;
    private final CourseStatusMapper courseStatusMapper;
    private final SubmissionService submissionService;

    public Optional<CourseDTO> findById(Long id) {
        return courseRepository.findById(id).map(courseMapper::toDto);
    }

    @Override
    public CourseDTO save(CourseDTO courseDTO) {
        Course course = courseMapper.toEntity(courseDTO);
        course = courseRepository.save(course);

        return courseMapper.toDto(course);
    }

    @Override
    public Page<CourseDTO> findAll(Filters filters, Pageable pageable) {
        return courseRepository.findAll(new SpecificationBuilder<Course>(filters).build(), pageable)
                .map(courseMapper::toDto);
    }

    @Override
    public List<CourseDTO> findByStudentId(Long studentId) {
        return courseMapper.toDto(courseRepository.findByStudentsId(studentId));
    }

    @Override
    public void delete(Long courseId) {
        courseRepository.deleteById(courseId);
    }

    @Override
    public void subscribeStudentToCourse(Long courseId, Long studentId) {
        Course targetCourse = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(StudentNotFoundException::new);

        Set<Course> studentCourses = student.getStudentCourses();

        if (studentCourses.contains(targetCourse)) {
            throw new StudentAlreadySubscribedToCourse();
        }

        if (studentCourses.size() > 5) {
            throw new CourseLimitExceededException();
        }

        targetCourse.addStudent(student);
        courseRepository.save(targetCourse);
    }

    @Override
    public CourseStatusDTO getCourseStatus(Long courseId, Long studentId) {
        if (!isStudentSubscribedToCourse(courseId, studentId)) {
            throw new StudentNotSubscribedToCourse();
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);
        CourseGradeDTO courseGradeDTO = calculateCourseStatus(studentId, course);
        return courseStatusMapper.toDto(course, studentId, courseGradeDTO);
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
    public boolean isStudentSubscribedToCourse(Long courseId, Long studentId) {
        return courseRepository.existsByIdAndStudentsId(courseId, studentId);
    }

    @Override
    public Set<? extends CourseDTO> getMyCourses(Long userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        if (currentUser.getRole().equals(Roles.STUDENT) && currentUser instanceof Student currentStudent) {
            return currentStudent.getStudentCourses().stream()
                            .map(course -> {
                                CourseGradeDTO courseGradeDTO = calculateCourseStatus(currentStudent.getId(), course);
                                return courseStatusMapper.toDto(course, currentStudent.getId(), courseGradeDTO);
                            })
                    .collect(Collectors.toSet());
        } else if (currentUser.getRole().equals(Roles.INSTRUCTOR) && currentUser instanceof Instructor currentInstructor) {
            return courseMapper.toDto(currentInstructor.getInstructorCourses());
        } else {
            throw new IllegalRoleAccessException();
        }
    }

    @Override
    public void addInstructorToCourse(Long courseId, Long instructorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(InstructorNotFoundException::new);
        course.addInstructor(instructor);
        course = courseRepository.save(course);
        courseMapper.toDto(course);
    }

    @Override
    public void deleteInstructorForCourse(Long courseId, Long instructorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(InstructorNotFoundException::new);
        course.removeInstructor(instructor);
        course = courseRepository.save(course);
        courseMapper.toDto(course);
    }
}
