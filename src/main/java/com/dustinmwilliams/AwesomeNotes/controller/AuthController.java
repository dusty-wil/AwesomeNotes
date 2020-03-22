package com.dustinmwilliams.AwesomeNotes.controller;

import com.dustinmwilliams.AwesomeNotes.dto.AuthenticationRequest;
import com.dustinmwilliams.AwesomeNotes.dto.LoginRequest;
import com.dustinmwilliams.AwesomeNotes.dto.RefreshTokenRequest;
import com.dustinmwilliams.AwesomeNotes.dto.RegisterRequest;
import com.dustinmwilliams.AwesomeNotes.service.AuthService;
import com.dustinmwilliams.AwesomeNotes.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController
{
    @Autowired
    private AuthService authService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody RegisterRequest regReq)
    {
        authService.signup(regReq);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationRequest login(@RequestBody LoginRequest loginRequest)
    {
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh/token")
    public AuthenticationRequest refreshTokens(@RequestBody RefreshTokenRequest refreshTokenRequest)
    {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshTokenRequest refreshTokenRequest)
    {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return new ResponseEntity<>("You have been logged out.", HttpStatus.OK);
    }
}
