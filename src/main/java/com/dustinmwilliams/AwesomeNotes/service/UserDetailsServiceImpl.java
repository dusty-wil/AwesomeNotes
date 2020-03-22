package com.dustinmwilliams.AwesomeNotes.service;

import com.dustinmwilliams.AwesomeNotes.model.User;
import com.dustinmwilliams.AwesomeNotes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;


@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException
    {
        User user = userRepository.findUserByUserName(s).orElseThrow(() -> {
            return new UsernameNotFoundException("User account does not exist");
        });

        return new org.springframework.security.core.userdetails.User(
            user.getUserName(),
            user.getPassword(),
            true,
            true,
            true,
            true,
            getAuthorities("ROLE_USER")
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role_user)
    {
        return Collections.singletonList(new SimpleGrantedAuthority(role_user));
    }
}
