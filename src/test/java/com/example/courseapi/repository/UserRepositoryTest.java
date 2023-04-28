package com.example.courseapi.repository;

import com.example.courseapi.config.DefaultJPARepositoryTestConfiguration;
import com.example.courseapi.domain.User;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.util.EntityCreatorUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unused")
@DefaultJPARepositoryTestConfiguration
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

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
    public void should_find_no_users_if_repository_is_empty() {
        List<User> users = userRepository.findAll();

        assertThat(users).isEmpty();
    }

    @Test
    public void should_store_a_user() {
        User user = userRepository.save(
                EntityCreatorUtil.createUser("1")
        );

        assertThat(user).hasFieldOrPropertyWithValue("firstName", "FirstName#1");
        assertThat(user).hasFieldOrPropertyWithValue("lastName", "LastName#1");
        assertThat(user).hasFieldOrPropertyWithValue("email", "user1@email.com");
        assertThat(user).hasFieldOrPropertyWithValue("role", Roles.STUDENT);
    }

    @Test
    public void should_find_all_users() {
        User user1 = entityManager.persist(
                EntityCreatorUtil.createUser("1")
        );

        User user2 = entityManager.persist(
                EntityCreatorUtil.createUser("2")
        );

        User user3 = entityManager.persist(
                EntityCreatorUtil.createUser("3")
        );

        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(3)
                .contains(user1, user2, user3);
    }

    @Test
    public void should_find_user_by_id() {
        User user1 = entityManager.persist(
                EntityCreatorUtil.createUser("1")
        );

        User user2 = entityManager.persist(
                EntityCreatorUtil.createUser("2")
        );

        Optional<User> foundUserOpt = userRepository.findById(user2.getId());
        assertThat(foundUserOpt).isPresent();

        User foundUser = foundUserOpt.get();
        assertThat(foundUser).isEqualTo(user2);
    }

    @Test
    public void should_find_user_by_email() {
        User user1 = entityManager.persist(
                EntityCreatorUtil.createUser("1")
        );

        User user2 = entityManager.persist(
                EntityCreatorUtil.createUser("2")
        );

        Optional<User> foundUserOpt = userRepository.findByEmail(user1.getEmail());
        assertThat(foundUserOpt).isPresent();

        User foundUser = foundUserOpt.get();
        assertThat(foundUser).isEqualTo(user1);
    }

    @Test
    public void should_update_user_by_id() {
        User user1 = entityManager.persist(
                EntityCreatorUtil.createUser("1")
        );

        User user2 = entityManager.persist(
                EntityCreatorUtil.createUser("2")
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
                EntityCreatorUtil.createUser("1")
        );

        User user2 = entityManager.persist(
                EntityCreatorUtil.createUser("2")
        );

        User user3 = entityManager.persist(
                EntityCreatorUtil.createUser("3")
        );

        userRepository.deleteById(user2.getId());

        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(2).contains(user1, user3);
    }

    @Test
    public void should_delete_all_users() {
        User user1 = entityManager.persist(
                EntityCreatorUtil.createUser("1")
        );

        User user2 = entityManager.persist(
                EntityCreatorUtil.createUser("2")
        );

        userRepository.deleteAll();

        assertThat(userRepository.count()).isEqualTo(0L);
    }
}
