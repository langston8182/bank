package com.cmarchive.bank.repository;

import com.cmarchive.bank.domain.PermanentOperation;
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
public class PermanentOperationRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PermanentOperationRepository permanentOperationRepository;

    @Test
    public void findByUserId_nominal() {
        User user = testEntityManager.persist(creerUser());
        PermanentOperation permanentOperation = testEntityManager.persist(creerPermanentOperation(user));
        testEntityManager.flush();

        List<PermanentOperation> permanentOperations = permanentOperationRepository.findByUserId(user.getId());

        assertThat(permanentOperations).hasSize(1);
        assertThat(permanentOperations.get(0).getIntitule()).isEqualToIgnoringCase(permanentOperation.getIntitule());
        assertThat(permanentOperations.get(0).getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    public void findAllByOrderByJourAsc_nominal() {
       List<PermanentOperation> permanentOperations = permanentOperationRepository.findAllByOrderByJourAsc();

        assertThat(permanentOperations).hasSize(4);
    }

    private User creerUser() {
        User user = new User()
                .setNom("Nom")
                .setPassword("password");

        return user;
    }

    private PermanentOperation creerPermanentOperation(User user) {
        PermanentOperation permanentOperation = new PermanentOperation()
                .setUser(user)
                .setIntitule("Intitule")
                .setJour(1);

        return permanentOperation;
    }
}