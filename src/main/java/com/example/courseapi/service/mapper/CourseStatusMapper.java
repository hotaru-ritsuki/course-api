package com.example.courseapi.service.mapper;

import com.example.courseapi.domain.*;
import com.example.courseapi.dto.CourseStatusDTO;
import com.example.courseapi.dto.CourseGradeDTO;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseStatusDTO}.
 */
@Mapper(componentModel = "spring", uses = CourseMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@Component
public interface CourseStatusMapper extends EntityMapper<CourseStatusDTO, Course> {

    @Mappings(value = {
            @Mapping(source = "course.instructors", target = "instructorIds", qualifiedByName = "usersToIds"),
            @Mapping(source = "course.students", target = "studentIds", qualifiedByName = "usersToIds"),
            @Mapping(source = "course.lessons", target = "lessonIds", qualifiedByName = "lessonsToIds"),
            @Mapping(source = "courseGradeDTO.courseStatus", target = "courseStatus"),
            @Mapping(source = "courseGradeDTO.finalGrade", target = "finalGrade")
    })
    CourseStatusDTO toDto(Course course, Long studentId, CourseGradeDTO courseGradeDTO);

    @Mappings(value = {
            @Mapping(source = "instructorIds", target = "instructors", qualifiedByName = "idsToInstructors"),
            @Mapping(source = "studentIds", target = "students", qualifiedByName = "idsToStudents"),
            @Mapping(source = "lessonIds", target = "lessons", qualifiedByName = "idsToLessons")
    })
    Course toEntity(CourseStatusDTO courseStatusDTO);

    default Course fromId(Long id) {
        if (id == null) {
            return null;
        }
        Course course = new Course();
        course.setId(id);
        return course;
    }
}
