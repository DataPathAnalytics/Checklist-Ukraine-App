package com.datapath.datasourceapp.controller;

import com.datapath.datasourceapp.request.SearchProperty;
import com.datapath.datasourceapp.response.DataSourceResponse;
import com.datapath.datasourceapp.service.SearchService;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
public class SearchController {

    private final SearchService service;

    @PostMapping("search")
    public List<Document> search(@RequestBody @Valid SearchProperty request) {
        return service.search(request);
    }

    @GetMapping("data-sources")
    public DataSourceResponse getDataSources() {
        return service.dataSources();
    }
}
