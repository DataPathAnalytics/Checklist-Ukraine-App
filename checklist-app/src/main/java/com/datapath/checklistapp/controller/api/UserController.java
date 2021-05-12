package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.UserDTO;
import com.datapath.checklistapp.dto.UserPageDTO;
import com.datapath.checklistapp.dto.UserStateDTO;
import com.datapath.checklistapp.dto.request.users.RegisterRequest;
import com.datapath.checklistapp.dto.request.users.ResetPasswordRequest;
import com.datapath.checklistapp.dto.request.users.ResetPasswordSendRequest;
import com.datapath.checklistapp.dto.request.users.UpdateRequest;
import com.datapath.checklistapp.service.web.UserWebService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UserController {

    private static final String DEFAULT_PAGE_SIZE = "10";

    private final UserWebService service;

    @GetMapping
    public UserPageDTO list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {
        return service.list(page, size);
    }

    @PostMapping("register")
    public void register(@Valid @RequestBody RegisterRequest request) {
        service.register(request);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('admin')")
    public UserPageDTO update(@RequestBody @Valid List<UpdateRequest> requests) {
        for (UpdateRequest request : requests) {
            service.update(request);
        }
        return service.list(0, Integer.parseInt(DEFAULT_PAGE_SIZE));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('admin')")
    public UserPageDTO delete(@PathVariable Integer id) {
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
