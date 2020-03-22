package com.dustinmwilliams.AwesomeNotes.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="`user`")  // https://stackoverflow.com/questions/3608420/hibernate-saving-user-model-to-postgres
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    @Size(max = 250)
    private String userName;

    @NotBlank
    @Column(unique = true)
    @Size(max = 250)
    private String email;

    @NotBlank
    @Column
    private String password;

    public User(@NotBlank String userName, @NotBlank String email, @NotBlank String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public User() { }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
