package com.datapath.analyticapp.dto.imported.user;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime registeredDate;
    private LocalDateTime dateModified;
    private List<EmploymentDTO> employments = new ArrayList<>();

    @Data
    public static class EmploymentDTO {
        private LocalDateTime start;
        private LocalDateTime end;
        private DepartmentDTO department;
    }

    @Data
    public static class DepartmentDTO {
        private Long identifier;
        private String name;
    }
}
