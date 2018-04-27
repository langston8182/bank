package com.cmarchive.bank.models.examples;

import com.cmarchive.bank.domain.PermanentOperation;
import com.cmarchive.bank.utils.Builder;
import com.github.javafaker.Faker;

public class PermanentOperationExample {

    public static Builder<PermanentOperation> aPermanentOperation() {
        Faker faker = new Faker();
        return Builder.build(PermanentOperation.class)
                .with(p -> p.setIntitule(faker.name().name()))
                .with(p -> p.setJour(faker.number().numberBetween(1, 28)))
                .with(p -> p.setPrix(faker.number().numberBetween(0, 1000)));
    }
    
}
