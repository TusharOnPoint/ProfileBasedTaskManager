package com.tushar.taskmanagerapp.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tushar.taskmanagerapp.exceptions.NotFoundException;
import com.tushar.taskmanagerapp.model.User;
import com.tushar.taskmanagerapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new NotFoundException("User not found."));
        
        return AuthUser.builder().user(user).build();
    }
    
}
