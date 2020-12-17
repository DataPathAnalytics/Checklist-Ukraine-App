package com.datapath.checklistukraineapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/")
public class DataController {

    @GetMapping
    public LocalDateTime get() {
        return LocalDateTime.now();
    }
}
