package com.datapath.datasourceapp.controller;

import com.datapath.datasourceapp.request.SearchProperty;
import com.datapath.datasourceapp.service.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("search")
@AllArgsConstructor
public class SearchController {

    private final SearchService service;

    @PostMapping
    public List<HashMap> search(@RequestBody @Valid List<SearchProperty> request) {
        return service.search(request);
    }
}
