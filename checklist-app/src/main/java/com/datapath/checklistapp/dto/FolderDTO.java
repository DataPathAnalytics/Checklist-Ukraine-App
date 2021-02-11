package com.datapath.checklistapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderDTO {

    private Long id;
    private String name;
    private LocalDateTime dateCreated;
}
