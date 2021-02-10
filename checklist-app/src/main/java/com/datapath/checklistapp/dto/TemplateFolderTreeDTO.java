package com.datapath.checklistapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TemplateFolderTreeDTO {
    private FolderDTO folder;
    private List<TemplateDTO> templates;
}
