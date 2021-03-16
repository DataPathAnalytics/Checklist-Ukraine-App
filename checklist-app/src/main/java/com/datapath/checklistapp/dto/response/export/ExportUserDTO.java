package com.datapath.checklistapp.dto.response.export;

import lombok.Data;

@Data
public class ExportUserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
}
