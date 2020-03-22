package com.dustinmwilliams.AwesomeNotes.model;


import javax.persistence.*;
import java.time.Instant;

@Entity
public class RefreshToken
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Instant createdDate;

    public RefreshToken(Long id, String token, Instant createdDate) {
        this.id = id;
        this.token = token;
        this.createdDate = createdDate;
    }

    public RefreshToken() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}
