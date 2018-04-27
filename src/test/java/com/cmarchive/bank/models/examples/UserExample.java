package com.cmarchive.bank.models.examples;

import com.cmarchive.bank.domain.User;
import com.cmarchive.bank.utils.Builder;
import com.github.javafaker.Faker;

public class UserExample {

    public static Builder<User> aUser() {
        Faker faker = new Faker();
        
        return Builder.build(User.class)
                .with(u -> u.setEmail(faker.internet().emailAddress()))
                .with(u -> u.setNom(faker.name().lastName()))
                .with(u -> u.setPassword(faker.name().name()))
                .with(u -> u.setPrenom(faker.name().firstName()));
    }
    
}
