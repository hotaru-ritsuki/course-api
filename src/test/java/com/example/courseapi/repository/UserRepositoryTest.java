package com.example.courseapi.repository;

import com.example.courseapi.config.DefaultRepositoryTestConfiguration;
import com.example.courseapi.domain.User;
import com.example.courseapi.domain.enums.Roles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unused")
@DefaultRepositoryTestConfiguration
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void should_find_no_users_if_repository_is_empty() {
        List<User> users = userRepository.findAll();

        assertThat(users).isEmpty();
    }

    @Test
    public void should_store_a_user() {
        User user = userRepository.save(
                User.builder()
                        .firstName("User FirstName")
                        .lastName("User LastName")
                        .email("user@email.com")
                        .password("User TestPassword")
                        .role(Roles.STUDENT)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        assertThat(user).hasFieldOrPropertyWithValue("firstName", "User FirstName");
        assertThat(user).hasFieldOrPropertyWithValue("lastName", "User LastName");
        assertThat(user).hasFieldOrPropertyWithValue("email", "user@email.com");
        assertThat(user).hasFieldOrPropertyWithValue("password", "User TestPassword");
        assertThat(user).hasFieldOrPropertyWithValue("role", Roles.STUDENT);
    }

    @Test
    public void should_find_all_users() {
        User user1 = entityManager.persist(
                User.builder()
                        .firstName("FirstName#1")
                        .lastName("LastName#1")
                        .email("user1@email.com")
                        .password("TestPassword#1")
                        .role(Roles.STUDENT)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        User user2 = entityManager.persist(
                User.builder()
                        .firstName("FirstName#2")
                        .lastName("LastName#2")
                        .email("user2@email.com")
                        .password("TestPassword#2")
                        .role(Roles.STUDENT)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        User user3 = entityManager.persist(
                User.builder()
                        .firstName("FirstName#3")
                        .lastName("LastName#3")
                        .email("user3@email.com")
                        .password("TestPassword#3")
                        .role(Roles.STUDENT)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(3)
                .contains(user1, user2, user3);
    }

    @Test
    public void should_find_user_by_id() {
        User user1 = entityManager.persist(
                User.builder()
                        .firstName("FirstName#1")
                        .lastName("LastName#1")
                        .email("user1@email.com")
                        .password("TestPassword#1")
                        .role(Roles.STUDENT)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        User user2 = entityManager.persist(
                User.builder()
                        .firstName("FirstName#2")
                        .lastName("LastName#2")
                        .email("user2@email.com")
                        .password("TestPassword#2")
                        .role(Roles.STUDENT)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Optional<User> foundUserOpt = userRepository.findById(user2.getId());
        assertThat(foundUserOpt).isPresent();

        User foundUser = foundUserOpt.get();
        assertThat(foundUser).isEqualTo(user2);
    }

    @Test
    public void should_find_user_by_email() {
        User user1 = entityManager.persist(
                User.builder()
                        .firstName("FirstName#1")
                        .lastName("LastName#1")
                        .email("user1@email.com")
                        .password("TestPassword#1")
                        .role(Roles.STUDENT)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        User user2 = entityManager.persist(
                User.builder()
                        .firstName("FirstName#2")
                        .lastName("LastName#2")
                        .email("user2@email.com")
                        .password("TestPassword#2")
                        .role(Roles.STUDENT)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Optional<User> foundUserOpt = userRepository.findByEmail(user1.getEmail());
        assertThat(foundUserOpt).isPresent();

        User foundUser = foundUserOpt.get();
        assertThat(foundUser).isEqualTo(user1);
    }

    @Test
    public void should_update_user_by_id() {
        User user1 = entityManager.persist(
                User.builder()
                        .firstName("FirstName#1")
                        .lastName("LastName#1")
                        .email("user1@email.com")
                        .password("TestPassword#1")
                        .role(Roles.STUDENT)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        User user2 = entityManager.persist(
                User.builder()
                        .firstName("FirstName#2")
                        .lastName("LastName#2")
                        .email("user2@email.com")
                        .password("TestPassword#2")
                        .role(Roles.STUDENT)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        Optional<User> userOpt = userRepository.findById(user2.getId());
        assertThat(userOpt).isPresent();

        User user = userOpt.get();
        user.setFirstName("UPDATED FIRSTNAME");
        user.setLastName("UPDATED LASTNAME");
        user.setRole(Roles.INSTRUCTOR);
        user.setEmail("UPDATEDEMAIL@email.com");
        user.setPassword("UPDATED PASSWORD");

        userRepository.save(user);

        Optional<User> checkUserOpt = userRepository.findById(user2.getId());
        assertThat(checkUserOpt).isPresent();

        User checkUser = checkUserOpt.get();

        assertThat(checkUser.getId()).isEqualTo(user.getId());
        assertThat(checkUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(checkUser.getLastName()).isEqualTo(user.getLastName());
        assertThat(checkUser.getRole()).isEqualTo(user.getRole());
        assertThat(checkUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(checkUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    public void should_delete_user_by_id() {
        User user1 = entityManager.persist(
                User.builder()
                        .firstName("FirstName#1")
                        .lastName("LastName#1")
                        .email("user1@email.com")
                        .password("TestPassword#1")
                        .role(Roles.STUDENT)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        User user2 = entityManager.persist(
                User.builder()
                        .firstName("FirstName#2")
                        .lastName("LastName#2")
                        .email("user2@email.com")
                        .password("TestPassword#2")
                        .role(Roles.STUDENT)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        User user3 = entityManager.persist(
                User.builder()
                        .firstName("FirstName#3")
                        .lastName("LastName#3")
                        .email("user3@email.com")
                        .password("TestPassword#3")
                        .role(Roles.STUDENT)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        userRepository.deleteById(user2.getId());

        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(2).contains(user1, user3);
    }

    @Test
    public void should_delete_all_users() {
        User user1 = entityManager.persist(
                User.builder()
                        .firstName("FirstName#1")
                        .lastName("LastName#1")
                        .email("user1@email.com")
                        .password("TestPassword#1")
                        .role(Roles.STUDENT)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        User user2 = entityManager.persist(
                User.builder()
                        .firstName("FirstName#2")
                        .lastName("LastName#2")
                        .email("user2@email.com")
                        .password("TestPassword#2")
                        .role(Roles.STUDENT)
                        .createdBy("Anonymous")
                        .createdDate(LocalDateTime.now())
                        .modifiedBy("Anonymous")
                        .modifiedDate(LocalDateTime.now())
                        .build()
        );

        userRepository.deleteAll();

        assertThat(userRepository.count()).isEqualTo(0L);
    }
}
