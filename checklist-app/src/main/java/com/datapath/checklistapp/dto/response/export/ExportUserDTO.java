package com.datapath.checklistapp.dto.response.export;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExportUserDTO {
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime registeredDate;
    private LocalDateTime dateModified;
}
