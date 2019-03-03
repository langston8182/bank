package com.cmarchive.bank.repository;

import com.cmarchive.bank.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByEmail_nominal() {
        User user = creerUser();
        testEntityManager.persist(user);
        testEntityManager.flush();

        User resultat = userRepository.findByEmail(user.getEmail());

        assertThat(resultat).isNotNull();
        assertThat(resultat.getNom()).isEqualToIgnoringCase(user.getNom());
        assertThat(resultat.getEmail()).isEqualToIgnoringCase(user.getEmail());
    }

    @Test
    public void findAllByOrderByNomAsc_nominal() {
        List<User> users = userRepository.findAllByOrderByNomAsc();

        assertThat(users).hasSize(2);
    }

    private User creerUser() {
        User user = new User()
                .setNom("Nom")
                .setPassword("password")
                .setEmail("test@gmail.com");

        return user;
    }
}