package com.demo.restapiusermanage;

import com.demo.restapiusermanage.entity.Role;
import com.demo.restapiusermanage.entity.User;
import com.demo.restapiusermanage.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {
    @Autowired
    IUserRepository userRepo;

    @Test
    public void testCreateUser(){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String password = passwordEncoder.encode("Tuoi1995");

        User newUser = new User(1L, "andrew", "xuan", "andrew@email.com", 28, password);

        User savedUser = userRepo.save(newUser);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);
    }
    @Test
    public void testAssignRoleToUser() {
        Long userId = 2L;
        Integer roleId = 3;
        User user = userRepo.findById(userId).get();
        user.addRole(new Role(roleId));

        User updatedUser = userRepo.save(user);
        assertThat(updatedUser.getRoles()).hasSize(2);

    }
}
