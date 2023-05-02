package com.example.courseapi.service.mapper;

import com.example.courseapi.domain.*;
import com.example.courseapi.dto.CourseGradeDTO;
import com.example.courseapi.dto.request.CourseRequestDTO;
import com.example.courseapi.dto.response.CourseResponseDTO;
import com.example.courseapi.dto.response.CourseStatusResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseResponseDTO}.
 */
@Mapper(componentModel = "spring", uses = LessonMapper.class)
@Component
public interface CourseMapper extends EntityMapper<CourseRequestDTO, CourseResponseDTO, Course> {
    @Mappings(value = {
            @Mapping(source = "instructors", target = "instructorIds", qualifiedByName = "usersToIds"),
            @Mapping(source = "students", target = "studentIds", qualifiedByName = "usersToIds"),
            @Mapping(source = "lessons", target = "lessons")
    })
    CourseResponseDTO toResponseDto(final Course course);

    @Mappings(value = {
            @Mapping(source = "instructors", target = "instructorIds", qualifiedByName = "usersToIds"),
            @Mapping(source = "students", target = "studentIds", qualifiedByName = "usersToIds")
    })
    CourseRequestDTO toRequestDto(final Course course);

    @Mappings(value = {
            @Mapping(source = "instructorIds", target = "instructors", qualifiedByName = "idsToInstructors"),
            @Mapping(source = "studentIds", target = "students", qualifiedByName = "idsToStudents"),
            @Mapping(source = "lessons", target = "lessons")
    })
    Course fromResponseDto(final CourseResponseDTO courseDTO);

    @Mappings(value = {
            @Mapping(source = "instructorIds", target = "instructors", qualifiedByName = "idsToInstructors"),
            @Mapping(source = "studentIds", target = "students", qualifiedByName = "idsToStudents")
    })
    Course fromRequestDto(final CourseRequestDTO courseDTO);

    @Mappings(value = {
            @Mapping(source = "course.instructors", target = "instructorIds", qualifiedByName = "usersToIds"),
            @Mapping(source = "course.students", target = "studentIds", qualifiedByName = "usersToIds"),
            @Mapping(source = "course.lessons", target = "lessons"),
            @Mapping(source = "courseGradeDTO.courseStatus", target = "courseStatus"),
            @Mapping(source = "courseGradeDTO.finalGrade", target = "finalGrade")
    })
    CourseStatusResponseDTO toResponseStatusDto(
            final Course course, final Long studentId, final CourseGradeDTO courseGradeDTO);

    @Mappings(value = {
            @Mapping(source = "instructorIds", target = "instructors", qualifiedByName = "idsToInstructors"),
            @Mapping(source = "studentIds", target = "students", qualifiedByName = "idsToStudents")
    })
    Course fromResponseStatusDto(final CourseStatusResponseDTO courseStatusDTO);

    default Course fromId(Long id) {
        if (id == null) {
            return null;
        }
        Course course = new Course();
        course.setId(id);
        return course;
    }

    @Named("usersToIds")
    static Set<Long> usersToIds(Set<? extends User> users) {
        if (Objects.isNull(users)) {
            return Collections.emptySet();
        }
        return users.stream().map(User::getId).collect(Collectors.toSet());
    }

    @Named("idsToStudents")
    static Set<Student> idsToStudents(Set<Long> usersIds) {
        if (Objects.isNull(usersIds)) {
            return Collections.emptySet();
        }
        return usersIds.stream().map(id -> Student.builder().id(id).build()).collect(Collectors.toSet());
    }
    @Named("idsToInstructors")
    static Set<Instructor> idsToInstructors(Set<Long> usersIds) {
        if (Objects.isNull(usersIds)) {
            return Collections.emptySet();
        }
        return usersIds.stream().map(id -> Instructor.builder().id(id).build()).collect(Collectors.toSet());
    }
}
