package com.datapath.analyticapp.dto.imported.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class UserApiResponse {

    private List<UserDTO> users;
    private LocalDateTime nextOffset;
}
