package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.UserDTO;
import com.datapath.checklistapp.dto.UserPageDTO;
import com.datapath.checklistapp.dto.UserStateDTO;
import com.datapath.checklistapp.dto.request.users.ResetPasswordRequest;
import com.datapath.checklistapp.dto.request.users.ResetPasswordSendRequest;
import com.datapath.checklistapp.dto.request.users.UserRegisterRequest;
import com.datapath.checklistapp.dto.request.users.UserUpdateRequest;
import com.datapath.checklistapp.service.UserWebService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("users")
@AllArgsConstructor
@Api(value = "Operation with users")
public class UserController {

    private static final String DEFAULT_PAGE_SIZE = "3";

    private final UserWebService service;

    @ApiOperation(value = "list of users", response = UserPageDTO.class)
    @GetMapping
    public UserPageDTO list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {
        return service.list(page, size);
    }

    @ApiOperation(value = "register new user")
    @PostMapping("register")
    public void register(@Valid @RequestBody UserRegisterRequest request) {
        service.register(request);
    }

    @ApiOperation(value = "update user", response = UserPageDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "Available only for users with role 'admin'")
    })
    @PutMapping
    @PreAuthorize("hasAuthority('admin')")
    public UserPageDTO update(@RequestBody @Valid List<UserUpdateRequest> requests) {
        for (UserUpdateRequest request : requests) {
            service.update(request);
        }
        return service.list(0, Integer.parseInt(DEFAULT_PAGE_SIZE));
    }

    @ApiOperation(value = "delete user", response = UserPageDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "Available only for users with role 'admin'")
    })
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('admin')")
    public UserPageDTO delete(@PathVariable Long id) {
        service.delete(id);
        return service.list(0, Integer.parseInt(DEFAULT_PAGE_SIZE));
    }

    @ApiOperation(value = "get users state", response = UserStateDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "Available only for users with role 'admin'")
    })
    @GetMapping("state")
    @PreAuthorize("hasAuthority('admin')")
    public UserStateDTO state() {
        return service.getState();
    }

    @ApiOperation(value = "send mail for reset password")
    @PostMapping("password/reset/mail")
    public void sendMailResetPassword(@Valid @RequestBody ResetPasswordSendRequest request) {
        service.sendMailForResetPassword(request);
    }

    @ApiOperation(value = "check user reset password request is available")
    @GetMapping("password/reset/check")
    public void checkResetPassword(@RequestParam String token) {
        service.checkTokenForResetPassword(token);
    }

    @ApiOperation(value = "save new password")
    @PostMapping("password/reset/save")
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        service.resetPassword(request);
    }

    @ApiOperation(value = "get current user description", response = UserDTO.class)
    @GetMapping("current")
    public UserDTO getCurrent() {
        return service.getCurrent();
    }
}
