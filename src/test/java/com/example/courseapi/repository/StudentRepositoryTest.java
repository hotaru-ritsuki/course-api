package com.example.courseapi.repository;

import com.example.courseapi.config.DefaultJPARepositoryTestConfiguration;
import com.example.courseapi.domain.Student;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.util.EntityCreatorUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unused")
@DefaultJPARepositoryTestConfiguration
public class StudentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    private AutoCloseable closable;

    @AfterEach
    public void destroy() throws Exception {
        closable.close();
    }

    @BeforeEach
    public void setup() {
        this.closable = MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_find_no_students_if_repository_is_empty() {
        List<Student> students = studentRepository.findAll();

        assertThat(students).isEmpty();
    }

    @Test
    @Transactional
    public void should_store_a_student() {
        Student student = studentRepository.save(
                EntityCreatorUtil.createStudent()
        );

        assertThat(student).hasFieldOrPropertyWithValue("firstName", "FirstName#");
        assertThat(student).hasFieldOrPropertyWithValue("lastName", "LastName#");
        assertThat(student).hasFieldOrPropertyWithValue("email", "student@email.com");
        assertThat(student).hasFieldOrPropertyWithValue("role", Roles.STUDENT);
    }

    @Test
    public void should_find_all_students() {
        Student student1 = entityManager.persist(
                EntityCreatorUtil.createStudent("1")
        );

        Student student2 = entityManager.persist(
                EntityCreatorUtil.createStudent("2")
        );

        Student student3 = entityManager.persist(
                EntityCreatorUtil.createStudent("3")
        );

        List<Student> students = studentRepository.findAll();

        assertThat(students).hasSize(3)
                .contains(student1, student2, student3);
    }

    @Test
    public void should_find_student_by_id() {
        Student student1 = entityManager.persist(
                EntityCreatorUtil.createStudent("1")
        );

        Student student2 = entityManager.persist(
                EntityCreatorUtil.createStudent("2")
        );

        Optional<Student> foundStudentOpt = studentRepository.findById(student2.getId());
        assertThat(foundStudentOpt).isPresent();

        Student foundStudent = foundStudentOpt.get();
        assertThat(foundStudent).isEqualTo(student2);
    }

    @Test
    public void should_find_student_by_email() {
        Student student1 = studentRepository.save(
                EntityCreatorUtil.createStudent("1")
        );

        Student student2 = studentRepository.save(
                EntityCreatorUtil.createStudent("2")
        );

        Optional<Student> foundStudentOpt = studentRepository.findByEmail(student1.getEmail());
        assertThat(foundStudentOpt).isPresent();

        Student foundStudent = foundStudentOpt.get();
        assertThat(foundStudent).isEqualTo(student1);
    }

    @Test
    public void should_update_student_by_id() {
        Student student1 = entityManager.persist(
                EntityCreatorUtil.createStudent("1")
        );

        Student student2 = entityManager.persist(
                EntityCreatorUtil.createStudent("2")
        );

        Optional<Student> studentOpt = studentRepository.findById(student2.getId());
        assertThat(studentOpt).isPresent();

        Student student = studentOpt.get();
        student.setFirstName("UPDATED FIRSTNAME");
        student.setLastName("UPDATED LASTNAME");
        student.setRole(Roles.INSTRUCTOR);
        student.setEmail("UPDATEDEMAIL@email.com");
        student.setPassword("UPDATED PASSWORD");

        studentRepository.save(student);

        Optional<Student> checkStudentOpt = studentRepository.findById(student2.getId());
        assertThat(checkStudentOpt).isPresent();

        Student checkStudent = checkStudentOpt.get();

        assertThat(checkStudent.getId()).isEqualTo(student.getId());
        assertThat(checkStudent.getFirstName()).isEqualTo(student.getFirstName());
        assertThat(checkStudent.getLastName()).isEqualTo(student.getLastName());
        assertThat(checkStudent.getRole()).isEqualTo(student.getRole());
        assertThat(checkStudent.getEmail()).isEqualTo(student.getEmail());
        assertThat(checkStudent.getPassword()).isEqualTo(student.getPassword());
    }

    @Test
    public void should_delete_student_by_id() {
        Student student1 = entityManager.persist(
                EntityCreatorUtil.createStudent("1")
        );

        Student student2 = entityManager.persist(
                EntityCreatorUtil.createStudent("2")
        );

        Student student3 = entityManager.persist(
                EntityCreatorUtil.createStudent("3")
        );

        studentRepository.deleteById(student2.getId());

        List<Student> students = studentRepository.findAll();

        assertThat(students).hasSize(2).contains(student1, student3);
    }

    @Test
    public void should_delete_all_students() {
        Student student1 = entityManager.persist(
                EntityCreatorUtil.createStudent("1")
        );

        Student student2 = entityManager.persist(
                EntityCreatorUtil.createStudent("2")
        );

        studentRepository.deleteAll();

        assertThat(studentRepository.count()).isEqualTo(0L);
    }
}
