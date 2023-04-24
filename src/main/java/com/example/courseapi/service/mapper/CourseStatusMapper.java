package com.example.courseapi.service.mapper;

import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.enums.CourseStatus;
import com.example.courseapi.dto.CourseDTO;
import com.example.courseapi.dto.CourseStatusDTO;
import com.example.courseapi.service.CourseService;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseStatusDTO}.
 */
@Mapper(componentModel = "spring")
@Component
public interface CourseStatusMapper extends EntityMapper<CourseStatusDTO, Course> {

    @Mapping(target = "courseStatus", ignore = true)
    CourseStatusDTO toDto(Course course, Long studentId, @Context CourseService courseService);

    Course toEntity(CourseStatusDTO courseStatusDTO);

    @AfterMapping
    default void toDTO(@MappingTarget CourseStatusDTO target, Course course, Long studentId, @Context CourseService courseService) {
        target.setCourseStatus(courseService.calculateCourseStatus(studentId, course));
    }

    default Course fromId(Long id) {
        if (id == null) {
            return null;
        }
        Course course = new Course();
        course.setId(id);
        return course;
    }
}
