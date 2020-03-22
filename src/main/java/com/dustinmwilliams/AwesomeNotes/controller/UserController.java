package com.dustinmwilliams.AwesomeNotes.controller;

import com.dustinmwilliams.AwesomeNotes.dto.UserRequest;
import com.dustinmwilliams.AwesomeNotes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController
{
    @Autowired
    private UserService userService;

    @PutMapping("/{id}")
    public ResponseEntity editUser(@RequestBody UserRequest userRequest)
    {
        userService.editUser(userRequest);
        return new ResponseEntity(HttpStatus.OK);
    }
}
