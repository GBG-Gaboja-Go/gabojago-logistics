package com.gbg.userservice.infrastructure.config.auth;

import com.gabojago.exception.AppException;
import com.gbg.userservice.domain.entity.User;
import com.gbg.userservice.domain.repository.UserRepository;
import com.gbg.userservice.infrastructure.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUserName(username).orElseThrow(
            () -> new AppException(UserErrorCode.USER_NOT_FOUND)
        );

        return new CustomUser(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            user.getRole()
        );
    }
}