package com.datapath.checklistapp.controller.export;

import com.datapath.checklistapp.dto.response.export.ExportChecklistDateResponse;
import com.datapath.checklistapp.dto.response.export.ExportControlActivityDTO;
import com.datapath.checklistapp.dto.response.export.ExportResponseSessionDTO;
import com.datapath.checklistapp.dto.response.export.ExportUserResponse;
import com.datapath.checklistapp.service.export.ExportService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("export")
@AllArgsConstructor
public class ExportController {

    private final ExportService service;

    @GetMapping("control-activities")
    public ExportChecklistDateResponse getControlActivityDates(
            @RequestParam(defaultValue = "2021-01-01T00:00:00.000", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime offset,
            @RequestParam(defaultValue = "100", required = false) int limit) {
        return service.getControlActivityDates(offset, limit);
    }

    @GetMapping("control-activities/{id}")
    public ExportControlActivityDTO getControlActivity(@PathVariable Long id) {
        return service.getControlActivity(id);
    }

    @GetMapping("response-sessions")
    public ExportChecklistDateResponse getResponseSessionDates(
            @RequestParam(defaultValue = "2021-01-01T00:00:00.000", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime offset,
            @RequestParam(defaultValue = "100", required = false) int limit) {
        return service.getResponseSessionDates(offset, limit);
    }

    @GetMapping("response-sessions/{id}")
    public ExportResponseSessionDTO getResponseSessionDates(@PathVariable Long id) {
        return service.getResponseSession(id);
    }

    @GetMapping("users")
    public ExportUserResponse getUpdatedUsers(
            @RequestParam(defaultValue = "2021-01-01T00:00:00.000", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime offset,
            @RequestParam(defaultValue = "10") int limit) {
        return service.getUpdateUsers(offset, limit);
    }
}
