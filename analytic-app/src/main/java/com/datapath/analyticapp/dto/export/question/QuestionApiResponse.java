package com.datapath.analyticapp.dto.export.question;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class QuestionApiResponse {

    private List<QuestionDTO> questions;
    private LocalDateTime nextOffset;
}
