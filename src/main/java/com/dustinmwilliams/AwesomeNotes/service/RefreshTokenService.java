package com.dustinmwilliams.AwesomeNotes.service;

import com.dustinmwilliams.AwesomeNotes.exception.TokenAuthException;
import com.dustinmwilliams.AwesomeNotes.model.RefreshToken;
import com.dustinmwilliams.AwesomeNotes.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Transactional
@Service
public class RefreshTokenService
{
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken()
    {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }

    public void validateRefreshToken(String token)
    {
        refreshTokenRepository.findByToken(token).orElseThrow(() -> {
            return new TokenAuthException("Invalid refresh token");
        });
    }

    public void deleteRefreshToken(String token)
    {
        refreshTokenRepository.deleteByToken(token);
    }
}
