package com.dustinmwilliams.AwesomeNotes.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

import static java.util.Date.from;

@Service
public class JwtProvider
{
    private Key key;

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;

    @PostConstruct
    public void init()
    {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String generateToken(Authentication auth)
    {
        User user = (User)auth.getPrincipal();
        return Jwts
            .builder()
            .setSubject(user.getUsername())
            .signWith(key)
            .setExpiration(from(Instant.now().plusMillis(jwtExpirationInMillis)))
            .compact()
        ;
    }

    public String generateTokenWithUserName(String username)
    {
        return Jwts
            .builder()
            .setSubject(username)
            .setIssuedAt(from(Instant.now()))
            .signWith(key)
            .setExpiration(from(Instant.now().plusMillis(jwtExpirationInMillis)))
            .compact()
        ;
    }

    public boolean validateToken(String jwt)
    {
        Jwts.parser().setSigningKey(key).parseClaimsJws(jwt);
        return true;
    }

    public String getUsernameFromJwt(String jwt)
    {
        Claims claims = Jwts
            .parser()
            .setSigningKey(key)
            .parseClaimsJws(jwt)
            .getBody()
        ;

        return claims.getSubject();
    }

    public Long getJwtExpirationInMillis() {
        return jwtExpirationInMillis;
    }
}
