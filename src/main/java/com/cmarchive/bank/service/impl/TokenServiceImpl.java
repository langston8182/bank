package com.cmarchive.bank.service.impl;

import com.cmarchive.bank.component.MyUserDetails;
import com.cmarchive.bank.domain.AuthenticationToken;
import com.cmarchive.bank.domain.User;
import com.cmarchive.bank.repository.AuthenticationTokenRepository;
import com.cmarchive.bank.service.TokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private AuthenticationTokenRepository authenticationTokenRepository;
    private ModelMapper modelMapper;

    @Autowired
    public TokenServiceImpl(AuthenticationTokenRepository authenticationTokenRepository,
                            ModelMapper modelMapper) {
        this.authenticationTokenRepository = authenticationTokenRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public AuthenticationToken generateToken(User user) {
        MyUserDetails userDetail = modelMapper.map(user, MyUserDetails.class);
        AuthenticationToken accessToken = new AuthenticationToken();
        accessToken.setUser(userDetail.getUser());
        accessToken.setToken(UUID.randomUUID().toString());

        return authenticationTokenRepository.save(accessToken);
    }

    @Override
    public User findUserByAccessToken(String token) {
        AuthenticationToken accessToken = authenticationTokenRepository.findByToken(token);

        if (accessToken == null) {
            return null;
        }

        return accessToken.getUser();
    }
}
