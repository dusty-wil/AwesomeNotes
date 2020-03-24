package com.dustinmwilliams.AwesomeNotes.security;

import com.dustinmwilliams.AwesomeNotes.exception.JwtKSException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;


import static java.util.Date.from;

@Service
public class JwtProvider
{
    private KeyStore keyStore;

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;

    @Value("${jks.keystore.password}")
    private String ksPw;

    @PostConstruct
    public void init()
    {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream stream = getClass().getResourceAsStream("/awesomenotes.jks");
            keyStore.load(stream, ksPw.toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new JwtKSException("There was a problem while loading the keystore.");
        }
    }

    public String generateToken(Authentication auth)
    {
        User user = (User)auth.getPrincipal();
        return Jwts
            .builder()
            .setSubject(user.getUsername())
            .signWith(getPrivateKey())
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
            .signWith(getPrivateKey())
            .setExpiration(from(Instant.now().plusMillis(jwtExpirationInMillis)))
            .compact()
        ;
    }

    public boolean validateToken(String jwt)
    {
        Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
        return true;
    }

    public String getUsernameFromJwt(String jwt)
    {
        Claims claims = Jwts
            .parser()
            .setSigningKey(getPublicKey())
            .parseClaimsJws(jwt)
            .getBody()
        ;

        return claims.getSubject();
    }

    public Long getJwtExpirationInMillis() {
        return jwtExpirationInMillis;
    }

    private PrivateKey getPrivateKey()
    {
        try {
            return (PrivateKey) keyStore.getKey("awesomenotes", ksPw.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new JwtKSException("There was a problem with retrieving key from keystore");
        }
    }

    private PublicKey getPublicKey()
    {
        try {
            return (PublicKey) keyStore.getCertificate("awesomenotes").getPublicKey();
        } catch (KeyStoreException e) {
            throw new JwtKSException("There was a problem with retrieving key from keystore");
        }
    }
}
