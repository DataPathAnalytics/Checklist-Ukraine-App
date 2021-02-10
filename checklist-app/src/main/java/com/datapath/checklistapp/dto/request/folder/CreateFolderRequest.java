package com.datapath.checklistapp.dto.request.folder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFolderRequest {

    @NotBlank
    private String name;
}
