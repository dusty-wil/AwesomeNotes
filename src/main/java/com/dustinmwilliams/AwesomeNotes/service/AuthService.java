package com.dustinmwilliams.AwesomeNotes.service;

import com.dustinmwilliams.AwesomeNotes.dto.AuthenticationRequest;
import com.dustinmwilliams.AwesomeNotes.dto.LoginRequest;
import com.dustinmwilliams.AwesomeNotes.dto.RefreshTokenRequest;
import com.dustinmwilliams.AwesomeNotes.dto.RegisterRequest;
import com.dustinmwilliams.AwesomeNotes.model.Note;
import com.dustinmwilliams.AwesomeNotes.model.User;
import com.dustinmwilliams.AwesomeNotes.repository.UserRepository;
import com.dustinmwilliams.AwesomeNotes.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.Optional;

@Transactional
@Service
public class AuthService
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private RefreshTokenService refreshTokenService;

    public void signup(RegisterRequest registerRequest)
    {
        if (userRepository.findUserByUserName(registerRequest.getUsername()).isPresent() ||
                userRepository.findUserByEmail(registerRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User already exists.");
        }

        User user = new User(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                encodePassword(registerRequest.getPassword())
        );
        userRepository.save(user);

    }

    public AuthenticationRequest login(LoginRequest loginRequest)
    {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(),
            loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = jwtProvider.generateToken(auth);

        return new AuthenticationRequest(
            token,
            refreshTokenService.generateRefreshToken().getToken(),
            Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()),
            loginRequest.getUsername()
        );
    }

    public AuthenticationRequest refreshToken(RefreshTokenRequest refreshTokenRequest)
    {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());

        return new AuthenticationRequest(
            token,
            refreshTokenRequest.getRefreshToken(),
            Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()),
            refreshTokenRequest.getUsername()
        );
    }

    public Optional<org.springframework.security.core.userdetails.User> getCurrentUser()
    {
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal()
                ;

        return Optional.of(user);
    }

    public String encodePassword(String password)
    {
        return passwordEncoder.encode(password);
    }
}
