package com.example.courseapi.service.impl;

import com.example.courseapi.domain.*;
import com.example.courseapi.domain.enums.CourseStatus;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.dto.CourseDTO;
import com.example.courseapi.dto.CourseStatusDTO;
import com.example.courseapi.exception.*;
import com.example.courseapi.repository.CourseRepository;
import com.example.courseapi.repository.StudentRepository;
import com.example.courseapi.repository.UserRepository;
import com.example.courseapi.service.CourseService;
import com.example.courseapi.service.SubmissionService;
import com.example.courseapi.service.mapper.CourseMapper;
import com.example.courseapi.service.mapper.CourseStatusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
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
    public List<CourseDTO> findAll() {
        return courseMapper.toDto(courseRepository.findAll());
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
        return courseStatusMapper.toDto(course);
    }

    @Override
    public CourseStatus calculateCourseStatus(Long studentId, Course course) {
        List<Submission> studentCourseSubmissions = submissionService.findAllByStudentIdAndCourseId(studentId, course.getId());

        int lessonsInCourse = course.getLessons().size();

        if (lessonsInCourse != studentCourseSubmissions.size()) {
            return CourseStatus.IN_PROGRESS;
        } else {
            double sumOfGrades = studentCourseSubmissions.stream().mapToDouble(Submission::getGrade).sum();
            return (sumOfGrades / lessonsInCourse) >= 80 ? CourseStatus.COMPLETED : CourseStatus.FAILED;
        }
    }

    @Override
    public boolean isStudentSubscribedToCourse(Long courseId, Long studentId) {
        return courseRepository.existsByIdAndStudentsId(courseId, studentId);
    }

    @Override
    public Set<CourseDTO> getMyCourses(Long userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        if (currentUser.getRole().equals(Roles.STUDENT) && currentUser instanceof Student currentStudent) {
            return courseMapper.toDto(currentStudent.getStudentCourses());
        } else if (currentUser.getRole().equals(Roles.INSTRUCTOR) && currentUser instanceof Instructor currentInstructor) {
            return courseMapper.toDto(currentInstructor.getInstructorCourses());
        } else {
            throw new IllegalRoleAccessException();
        }
    }
}
