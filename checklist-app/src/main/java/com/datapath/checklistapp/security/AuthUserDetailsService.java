package com.datapath.checklistapp.security;

import com.datapath.checklistapp.dao.entity.UserEntity;
import com.datapath.checklistapp.dao.service.UserDaoService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {

    private UserDaoService service;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity user = service.findActiveByEmail(username);

        if (isNull(user)) throw new UsernameNotFoundException(username);

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .disabled(user.isDisable())
                .accountLocked(user.isLocked())
                .authorities(user.getPermissions().stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.getRole()))
                        .collect(Collectors.toList()))
                .build();
    }

}