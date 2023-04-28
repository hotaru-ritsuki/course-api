package com.example.courseapi.service.mapper;

import com.example.courseapi.domain.Course;
import com.example.courseapi.domain.Student;
import com.example.courseapi.domain.User;
import com.example.courseapi.dto.StudentResponseDTO;
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
 * Mapper for the entity {@link Student} and its DTO {@link StudentResponseDTO}.
 */
@Mapper(componentModel = "spring")
@Component
public interface StudentMapper extends EntityMapper<StudentResponseDTO, Student> {

    @Mapping(source = "studentCourses", target = "studentCourseIds", qualifiedByName = "coursesToIds")
    StudentResponseDTO toDto(Student student);

    @Named("coursesToIds")
    static Set<Long> coursesToIds(Set<Course> studentCourses) {
        if (Objects.isNull(studentCourses)) {
            return Collections.emptySet();
        }
        return studentCourses.stream().map(Course::getId).collect(Collectors.toSet());
    }

    default Student fromId(Long id) {
        if (id == null) {
            return null;
        }
        Student student = new Student();
        student.setId(id);
        return student;
    }
}
