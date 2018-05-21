package com.cmarchive.bank.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class AuthenticationToken {

    @GeneratedValue
    @Id
    private Long id;

    private String token;

    @ManyToOne
    private User user;

    @Column
    private LocalDate expireDate;
}
