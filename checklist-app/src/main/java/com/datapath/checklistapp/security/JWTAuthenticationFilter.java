package com.datapath.checklistapp.security;

import com.datapath.checklistapp.dao.entity.UserEntity;
import com.datapath.checklistapp.dao.service.UserDaoService;
import com.datapath.checklistapp.dto.request.users.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import static com.datapath.checklistapp.security.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserDaoService userDaoService;

    @Autowired
    public void setUserDaoService(UserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        LoginRequest user = parseRequest(request);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                user.getEmail(), user.getPassword());

        setDetails(request, authRequest);

        try {
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (BadCredentialsException | LockedException e) {
            addInfoToAuthenticationErrorResponse(response, BAD_CREDENTIALS_ERROR_JSON_MESSAGE);
            return null;
        } catch (DisabledException e) {
            addInfoToAuthenticationErrorResponse(response, USER_IS_DISABLED_ERROR_JSON_MESSAGE);
            return null;
        }
    }

    private void addInfoToAuthenticationErrorResponse(HttpServletResponse response, String errorMessage) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        try (PrintWriter writer = response.getWriter()) {
            writer.write(errorMessage);
        } catch (IOException responseWriterException) {
            responseWriterException.printStackTrace();
        }
    }

    private LoginRequest parseRequest(HttpServletRequest request) {
        try {
            return new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

        } catch (IOException e) {
            throw new RuntimeException("Can't parse login data from request");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) {

        UserEntity user = userDaoService.findActiveByEmail(((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername());

        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .setSubject(((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername())
                .setId(user.getId().toString())
                .claim("permissions", authResult.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .compact();

        response.addHeader(HttpHeaders.AUTHORIZATION, SecurityConstants.TOKEN_PREFIX + token);
        UsersStorageService.addUser(user.getId());
    }
}
