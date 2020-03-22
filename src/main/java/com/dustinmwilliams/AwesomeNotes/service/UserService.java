package com.dustinmwilliams.AwesomeNotes.service;

import com.dustinmwilliams.AwesomeNotes.dto.UserRequest;
import com.dustinmwilliams.AwesomeNotes.model.User;
import com.dustinmwilliams.AwesomeNotes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    public void editUser(UserRequest userRequest)
    {
        authService
            .getCurrentUser()
            .map(user -> {
                User userToEdit = userRepository.findUserByUserName(user.getUsername()).orElseThrow(() -> {
                   return new UsernameNotFoundException("User doesn't exist.");
                });

                Optional<User> checkUser = userRepository.findUserByEmail(userRequest.getEmail());
                if (checkUser.isPresent() && !checkUser.get().getId().equals(userToEdit.getId())) {
                    throw new IllegalArgumentException("Email already exists.");
                }

                checkUser = userRepository.findUserByUserName(userRequest.getUsername());
                if (checkUser.isPresent() && !checkUser.get().getId().equals(userToEdit.getId())) {
                    throw new IllegalArgumentException("Username already exists.");
                }

                userToEdit.setUserName(userRequest.getUsername());
                userToEdit.setPassword(authService.encodePassword(userRequest.getPassword()));
                userToEdit.setEmail(userRequest.getEmail());

                return userRepository.save(userToEdit);
            }).orElseThrow(() -> new IllegalArgumentException("Not logged in."));
    }
}
