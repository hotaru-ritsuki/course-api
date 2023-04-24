package com.example.courseapi.service.mapper;

import com.example.courseapi.domain.Course;
import com.example.courseapi.dto.CourseDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseDTO}.
 */
@Mapper(componentModel = "spring")
@Component
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {

    CourseDTO toDto(Course course);

    Course toEntity(CourseDTO courseDTO);

    default Course fromId(Long id) {
        if (id == null) {
            return null;
        }
        Course course = new Course();
        course.setId(id);
        return course;
    }
}
