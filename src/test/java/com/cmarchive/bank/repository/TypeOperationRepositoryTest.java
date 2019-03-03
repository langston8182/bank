package com.cmarchive.bank.repository;

import com.cmarchive.bank.domain.TypeOperation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TypeOperationRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private TypeOperationRepository typeOperationRepository;

    @Test
    public void findByValueType_nominal() {
        TypeOperation typeOperation = creerTypeOperation();
        testEntityManager.persist(typeOperation);
        testEntityManager.flush();

        TypeOperation resultat = typeOperationRepository.findByValueType(typeOperation.getValue());

        assertThat(resultat).isNotNull();
        assertThat(resultat.getValue()).isEqualToIgnoringCase(typeOperation.getValue());
    }

    private TypeOperation creerTypeOperation() {
        TypeOperation typeOperation = new TypeOperation()
                .setValue("typeOperation");

        return typeOperation;
    }
}