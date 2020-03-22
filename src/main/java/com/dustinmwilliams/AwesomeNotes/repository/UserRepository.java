package com.dustinmwilliams.AwesomeNotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import  java.util.Optional;

import com.dustinmwilliams.AwesomeNotes.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findUserByUserName(String userName);
    Optional<User> findUserByEmail(String email);
}
