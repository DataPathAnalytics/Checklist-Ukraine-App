package com.datapath.checklistukraineapp.security;

import com.datapath.checklistukraineapp.dao.entity.UserEntity;
import com.datapath.checklistukraineapp.dao.service.UserDaoService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

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
                .authorities(new SimpleGrantedAuthority(user.getRole()))
                .build();
    }

}