package com.cmarchive.bank.repository;

import com.cmarchive.bank.domain.Operation;
import com.cmarchive.bank.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OperationRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private OperationRepository operationRepository;

    @Test
    public void findByUserId_nominal() {
        User user = testEntityManager.persist(creerUser());
        Operation operation = testEntityManager.persist(creerOperation(user));
        testEntityManager.flush();

        List<Operation> operations = operationRepository.findByUserId(user.getId());

        assertThat(operations).hasSize(1);
        assertThat(operations.get(0).getPrix()).isEqualTo(operation.getPrix());
        assertThat(operations.get(0).getUser().getNom()).isEqualToIgnoringCase(user.getNom());
    }

    @Test
    public void findAllByOrderByDateOperationDescIntituleAsc_nominal() {
        User user = testEntityManager.persist(creerUser());
        Operation operation = testEntityManager.persist(creerOperation(user));
        testEntityManager.flush();

        List<Operation> operations = operationRepository.findAllByOrderByDateOperationDescIntituleAsc();

        assertThat(operations).hasSize(1);
        assertThat(operations.get(0).getPrix()).isEqualTo(operation.getPrix());
        assertThat(operations.get(0).getUser().getNom()).isEqualToIgnoringCase(user.getNom());
    }

    @Test
    public void findByUserIdAndDateOperationBetweenOrderByDateOperationAscIntituleAsc_nominal() {
        User user = testEntityManager.persist(creerUser());
        Operation operation = testEntityManager.persist(creerOperation(user));
        testEntityManager.flush();
        LocalDate start = LocalDate.of(1999, 1, 1);
        LocalDate end = LocalDate.of(2001, 1, 1);

        List<Operation> operations = operationRepository
                .findByUserIdAndDateOperationBetweenOrderByDateOperationAscIntituleAsc(user.getId(), start, end);

        assertThat(operations).hasSize(1);
        assertThat(operations.get(0).getPrix()).isEqualTo(operation.getPrix());
        assertThat(operations.get(0).getUser().getNom()).isEqualToIgnoringCase(user.getNom());
    }

    @Test
    public void findByUserIdAndDateOperationBetweenOrderByDateOperationAscIntituleAsc_aucunResultat() {
        User user = testEntityManager.persist(creerUser());
        testEntityManager.persist(creerOperation(user));
        testEntityManager.flush();
        LocalDate start = LocalDate.of(2000, 2, 1);
        LocalDate end = LocalDate.of(2001, 1, 1);

        List<Operation> operations = operationRepository
                .findByUserIdAndDateOperationBetweenOrderByDateOperationAscIntituleAsc(user.getId(), start, end);

        assertThat(operations).isEmpty();
    }

    private User creerUser() {
        User user = new User()
                .setNom("Nom")
                .setPassword("password");

        return user;
    }

    private Operation creerOperation(User user) {
        Operation operation = new Operation()
                .setDateOperation(LocalDate.of(2000, 1, 1))
                .setPrix(1)
                .setIntitule("Intitule")
                .setUser(user)
                .setDateOperation(LocalDate.of(2000, 1, 1));

        return operation;
    }
}