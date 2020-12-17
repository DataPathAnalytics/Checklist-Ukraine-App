package com.datapath.checklistukraineapp.controller;

import com.datapath.checklistukraineapp.domain.request.UserRegisterRequest;
import com.datapath.checklistukraineapp.service.UserWebService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserController {

    private final UserWebService service;

    @PostMapping("register")
    public void register(@Valid @RequestBody UserRegisterRequest request) {
        service.register(request);
    }
}
