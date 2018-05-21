package com.cmarchive.bank.service;

import com.cmarchive.bank.domain.AuthenticationToken;
import com.cmarchive.bank.domain.User;

public interface TokenService {

    AuthenticationToken generateToken(User user);

    User findUserByAccessToken(String token);
}
