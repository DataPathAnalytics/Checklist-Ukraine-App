package com.datapath.checklistapp.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageTemplate {

    private String email;
    private String subject;
    private String text;
}