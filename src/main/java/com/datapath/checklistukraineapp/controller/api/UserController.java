package com.datapath.checklistukraineapp.controller.api;

import com.datapath.checklistukraineapp.dto.UserDTO;
import com.datapath.checklistukraineapp.dto.UserStateDTO;
import com.datapath.checklistukraineapp.dto.request.users.ResetPasswordRequest;
import com.datapath.checklistukraineapp.dto.request.users.ResetPasswordSendRequest;
import com.datapath.checklistukraineapp.dto.request.users.UserRegisterRequest;
import com.datapath.checklistukraineapp.dto.request.users.UserUpdateRequest;
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
    public void register(@Valid @RequestBody UserRegisterRequest request) {
        service.register(request);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('admin')")
    public List<UserDTO> update(@RequestBody @Valid List<UserUpdateRequest> requests) {
        for (UserUpdateRequest request : requests) {
            service.update(request);
        }
        return service.list();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('admin')")
    public List<UserDTO> delete(@PathVariable Long id) {
        service.delete(id);
        return service.list();
    }

    @GetMapping("state")
    @PreAuthorize("hasAuthority('admin')")
    public UserStateDTO state() {
        return service.getState();
    }

    @PostMapping("password/reset/mail")
    public void sendMailResetPassword(@Valid @RequestBody ResetPasswordSendRequest request) {
        service.sendMailForResetPassword(request);
    }

    @GetMapping("password/reset/check")
    public void checkResetPassword(@RequestParam String token) {
        service.checkTokenForResetPassword(token);
    }

    @PostMapping("password/reset/save")
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        service.resetPassword(request);
    }
}
