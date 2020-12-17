package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.node.UserNode;
import com.datapath.checklistukraineapp.dao.service.UserDaoService;
import com.datapath.checklistukraineapp.domain.request.UserRegisterRequest;
import com.datapath.checklistukraineapp.util.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserWebService {

    private final UserDaoService service;
    private final BCryptPasswordEncoder passwordEncoder;

    public void register(UserRegisterRequest request) {
        UserNode user = new UserNode();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDisable(true);
        user.setRole(UserRole.USER.getValue());
        service.save(user);
    }
}
