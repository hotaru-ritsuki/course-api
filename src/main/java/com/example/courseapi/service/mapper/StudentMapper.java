package com.example.courseapi.service.mapper;

import com.example.courseapi.domain.Student;
import com.example.courseapi.dto.StudentDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity {@link Student} and its DTO {@link StudentDTO}.
 */
@Mapper(componentModel = "spring")
@Component
public interface StudentMapper extends EntityMapper<StudentDTO, Student> {

    StudentDTO toDto(Student student);

    Student toEntity(StudentDTO studentDTO);

    default Student fromId(Long id) {
        if (id == null) {
            return null;
        }
        Student student = new Student();
        student.setId(id);
        return student;
    }
}
