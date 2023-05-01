package com.example.courseapi.service.mapper;

import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.Instructor;
import com.example.courseapi.dto.request.UserRequestDTO;
import com.example.courseapi.dto.response.InstructorResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link Instructor} and its DTO {@link InstructorResponseDTO}.
 */
@Mapper(componentModel = "spring")
@Component
public interface InstructorMapper extends EntityMapper<UserRequestDTO, InstructorResponseDTO, Instructor> {

    @Mapping(source = "instructorCourses", target = "instructorCourseIds", qualifiedByName = "coursesToIds")
    InstructorResponseDTO toResponseDto(Instructor instructor);

    @Named("coursesToIds")
    static Set<Long> coursesToIds(Set<Course> instructorCourses) {
        if (Objects.isNull(instructorCourses)) {
            return Collections.emptySet();
        }
        return instructorCourses.stream().map(Course::getId).collect(Collectors.toSet());
    }

    default Instructor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Instructor instructor = new Instructor();
        instructor.setId(id);
        return instructor;
    }
}