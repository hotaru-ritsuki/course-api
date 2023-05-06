package com.example.courseapi.repository;

import com.example.courseapi.config.PostgresRepositoryTestContainer;
import com.example.courseapi.config.annotation.DefaultJPARepositoryTestConfiguration;
import com.example.courseapi.domain.Instructor;
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
public class InstructorRepositoryTest extends PostgresRepositoryTestContainer {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InstructorRepository instructorRepository;

    private AutoCloseable closable;

    @AfterEach
    public void destroy() throws Exception {
        closable.close();
    }

    @BeforeEach
    public void setup() {
        this.closable = MockitoAnnotations.openMocks(this);
    }
    @Transactional
    @Test
    public void should_find_no_instructors_if_repository_is_empty() {
        List<Instructor> instructors = instructorRepository.findAll();

        assertThat(instructors).isEmpty();
    }

    @Transactional
    @Test
    public void should_store_a_instructor() {
        Instructor instructor = instructorRepository.save(
                EntityCreatorUtil.createInstructor()
        );

        assertThat(instructor).hasFieldOrPropertyWithValue("firstName", "FirstName#");
        assertThat(instructor).hasFieldOrPropertyWithValue("lastName", "LastName#");
        assertThat(instructor).hasFieldOrPropertyWithValue("email", "instructor@email.com");
        assertThat(instructor).hasFieldOrPropertyWithValue("role", Roles.INSTRUCTOR);
    }

    @Transactional
    @Test
    public void should_find_all_instructors() {
        Instructor instructor1 = entityManager.persist(
                EntityCreatorUtil.createInstructor("1")
        );

        Instructor instructor2 = entityManager.persist(
                EntityCreatorUtil.createInstructor("2")
        );

        Instructor instructor3 = entityManager.persist(
                EntityCreatorUtil.createInstructor("3")
        );

        List<Instructor> instructors = instructorRepository.findAll();

        assertThat(instructors).hasSize(3)
                .contains(instructor1, instructor2, instructor3);
    }

    @Transactional
    @Test
    public void should_find_instructor_by_id() {
        Instructor instructor1 = entityManager.persist(
                EntityCreatorUtil.createInstructor("1")
        );

        Instructor instructor2 = entityManager.persist(
                EntityCreatorUtil.createInstructor("2")
        );

        Optional<Instructor> foundInstructorOpt = instructorRepository.findById(instructor2.getId());
        assertThat(foundInstructorOpt).isPresent();

        Instructor foundInstructor = foundInstructorOpt.get();
        assertThat(foundInstructor).isEqualTo(instructor2);
    }

    @Transactional
    @Test
    public void should_find_instructor_by_email() {
        Instructor instructor1 = entityManager.persist(
                EntityCreatorUtil.createInstructor("1")
        );

        Instructor instructor2 = entityManager.persist(
                EntityCreatorUtil.createInstructor("2")
        );

        Optional<Instructor> foundInstructorOpt = instructorRepository.findByEmail(instructor1.getEmail());
        assertThat(foundInstructorOpt).isPresent();

        Instructor foundInstructor = foundInstructorOpt.get();
        assertThat(foundInstructor).isEqualTo(instructor1);
    }

    @Transactional
    @Test
    public void should_update_instructor_by_id() {
        Instructor instructor1 = entityManager.persist(
                EntityCreatorUtil.createInstructor("1")
        );

        Instructor instructor2 = entityManager.persist(
                EntityCreatorUtil.createInstructor("2")
        );

        Optional<Instructor> instructorOpt = instructorRepository.findById(instructor2.getId());
        assertThat(instructorOpt).isPresent();

        Instructor instructor = instructorOpt.get();
        instructor.setFirstName("UPDATED FIRSTNAME");
        instructor.setLastName("UPDATED LASTNAME");
        instructor.setRole(Roles.INSTRUCTOR);
        instructor.setEmail("UPDATEDEMAIL@email.com");
        instructor.setPassword("UPDATED PASSWORD");

        instructorRepository.save(instructor);

        Optional<Instructor> checkInstructorOpt = instructorRepository.findById(instructor2.getId());
        assertThat(checkInstructorOpt).isPresent();

        Instructor checkInstructor = checkInstructorOpt.get();

        assertThat(checkInstructor.getId()).isEqualTo(instructor.getId());
        assertThat(checkInstructor.getFirstName()).isEqualTo(instructor.getFirstName());
        assertThat(checkInstructor.getLastName()).isEqualTo(instructor.getLastName());
        assertThat(checkInstructor.getRole()).isEqualTo(instructor.getRole());
        assertThat(checkInstructor.getEmail()).isEqualTo(instructor.getEmail());
        assertThat(checkInstructor.getPassword()).isEqualTo(instructor.getPassword());
    }

    @Transactional
    @Test
    public void should_delete_instructor_by_id() {
        Instructor instructor1 = entityManager.persist(
                EntityCreatorUtil.createInstructor("1")
        );

        Instructor instructor2 = entityManager.persist(
                EntityCreatorUtil.createInstructor("2")
        );

        Instructor instructor3 = entityManager.persist(
                EntityCreatorUtil.createInstructor("3")
        );

        instructorRepository.deleteById(instructor2.getId());

        List<Instructor> instructors = instructorRepository.findAll();

        assertThat(instructors).hasSize(2).contains(instructor1, instructor3);
    }

    @Transactional
    @Test
    public void should_delete_all_instructors() {
        Instructor instructor1 = entityManager.persist(
                EntityCreatorUtil.createInstructor("1")
        );

        Instructor instructor2 = entityManager.persist(
                EntityCreatorUtil.createInstructor("2")
        );

        instructorRepository.deleteAll();

        assertThat(instructorRepository.count()).isEqualTo(0L);
    }
}
