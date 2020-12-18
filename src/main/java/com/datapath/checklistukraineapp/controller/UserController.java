package com.datapath.checklistukraineapp.controller;

import com.datapath.checklistukraineapp.domain.dto.UserDTO;
import com.datapath.checklistukraineapp.domain.request.UserRegisterRequest;
import com.datapath.checklistukraineapp.domain.request.UserUpdateRequest;
import com.datapath.checklistukraineapp.exception.DepartmentException;
import com.datapath.checklistukraineapp.exception.UserException;
import com.datapath.checklistukraineapp.service.UserWebService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UserController {

    private final UserWebService service;

    @GetMapping
    public List<UserDTO> list() {
        return service.list();
    }

    @PostMapping("register")
    public void register(@Valid @RequestBody UserRegisterRequest request) throws UserException, DepartmentException {
        service.register(request);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('admin')")
    public List<UserDTO> update(@RequestBody @Valid List<UserUpdateRequest> requests) throws UserException, DepartmentException {
        for (UserUpdateRequest request : requests) {
            service.update(request);
        }
        return service.list();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('admin')")
    public List<UserDTO> delete(@PathVariable Long id) throws UserException {
        service.delete(id);
        return service.list();
    }
}
