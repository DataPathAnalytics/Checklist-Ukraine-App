package com.datapath.checklistapp.controller.export;

import com.datapath.checklistapp.dto.response.export.ExportControlActivityResponse;
import com.datapath.checklistapp.dto.response.export.ExportQuestionResponse;
import com.datapath.checklistapp.dto.response.export.ExportUserDTO;
import com.datapath.checklistapp.service.export.ExportService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("export")
@AllArgsConstructor
public class ExportController {

    private final ExportService service;

    @GetMapping("questions")
    public ExportQuestionResponse getNewQuestions(
            @RequestParam(defaultValue = "2021-01-01T00:00:00.000", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime offset,
            @RequestParam(defaultValue = "10", required = false) int limit) {
        return service.getNewQuestions(offset, limit);
    }

    @GetMapping("activity-sessions")
    public ExportControlActivityResponse getUpdatedSession(
            @RequestParam(defaultValue = "2021-01-01T00:00:00.000", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime offset,
            @RequestParam(defaultValue = "10", required = false) int limit) {
        return service.getUpdatedSession(offset, limit);
    }

    @GetMapping("users")
    public List<ExportUserDTO> getUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return service.getUsers(page, size);
    }
}
