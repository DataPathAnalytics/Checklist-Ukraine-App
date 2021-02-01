package com.datapath.checklistukraineapp.controller.api;

import com.datapath.checklistukraineapp.dto.UserDTO;
import com.datapath.checklistukraineapp.dto.UserPageDTO;
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

    private static final String DEFAULT_PAGE_SIZE = "3";

    private final UserWebService service;

    @GetMapping
    public UserPageDTO list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {
        return service.list(page, size);
    }

    @PostMapping("register")
    public void register(@Valid @RequestBody UserRegisterRequest request) {
        service.register(request);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('admin')")
    public UserPageDTO update(@RequestBody @Valid List<UserUpdateRequest> requests) {
        for (UserUpdateRequest request : requests) {
            service.update(request);
        }
        return service.list(0, Integer.parseInt(DEFAULT_PAGE_SIZE));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('admin')")
    public UserPageDTO delete(@PathVariable Long id) {
        service.delete(id);
        return service.list(0, Integer.parseInt(DEFAULT_PAGE_SIZE));
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

    @GetMapping("current")
    public UserDTO getCurrent() {
        return service.getCurrent();
    }
}
