package com.datapath.analyticapp.service.imported;

import com.datapath.analyticapp.service.imported.question.QuestionImportService;
import com.datapath.analyticapp.service.imported.user.UserImportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ImportManageService {

    private final QuestionImportService questionImportService;
    private final UserImportService userImportService;

//    @Scheduled(fixedDelay = 1000 * 60)
    public void update() {
        userImportService.updateUsers();
        questionImportService.updateQuestions();
    }
}
