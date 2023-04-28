package com.example.courseapi.service.mapper;

import com.example.courseapi.domain.*;
import com.example.courseapi.dto.CourseDTO;
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
 * Mapper for the entity {@link Course} and its DTO {@link CourseDTO}.
 */
@Mapper(componentModel = "spring")
@Component
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {

    @Mappings(value = {
            @Mapping(source = "instructors", target = "instructorIds", qualifiedByName = "usersToIds"),
            @Mapping(source = "students", target = "studentIds", qualifiedByName = "usersToIds"),
            @Mapping(source = "lessons", target = "lessonIds", qualifiedByName = "lessonsToIds")
    })
    CourseDTO toDto(Course course);

    @Mappings(value = {
            @Mapping(source = "instructorIds", target = "instructors", qualifiedByName = "idsToInstructors"),
            @Mapping(source = "studentIds", target = "students", qualifiedByName = "idsToStudents"),
            @Mapping(source = "lessonIds", target = "lessons", qualifiedByName = "idsToLessons")
    })
    Course toEntity(CourseDTO courseDTO);

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

    @Named("lessonsToIds")
    static Set<Long> lessonsToIds(Set<Lesson> users) {
        if (Objects.isNull(users)) {
            return Collections.emptySet();
        }
        return users.stream().map(Lesson::getId).collect(Collectors.toSet());
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

    @Named("idsToLessons")
    static Set<Lesson> idsToLessons(Set<Long> lessonIds) {
        if (Objects.isNull(lessonIds)) {
            return Collections.emptySet();
        }
        return lessonIds.stream().map(id -> Lesson.builder().id(id).build()).collect(Collectors.toSet());
    }
}
