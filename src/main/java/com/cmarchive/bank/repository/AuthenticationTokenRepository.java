package com.cmarchive.bank.repository;

import com.cmarchive.bank.domain.AuthenticationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationTokenRepository extends JpaRepository<AuthenticationToken, Long> {

    AuthenticationToken findByToken(String authenticationToken);

}
