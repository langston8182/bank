package com.cmarchive.bank.models.examples;

import java.time.LocalDate;
import java.util.Date;

import com.cmarchive.bank.domain.Operation;
import com.cmarchive.bank.utils.Builder;
import com.github.javafaker.Faker;

public class OperationExample {

    public static Builder<Operation> aOperation() {
        Faker faker = new Faker();
        
        return Builder.build(Operation.class)
                .with(o -> o.setDateOperation(LocalDate.now()))
                .with(o -> o.setIntitule(faker.name().name()))
                .with(o -> o.setPrix(faker.number().numberBetween(0, 1000)));
    }
    
}
