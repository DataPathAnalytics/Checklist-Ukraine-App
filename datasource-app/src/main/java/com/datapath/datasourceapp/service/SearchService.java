package com.datapath.datasourceapp.service;

import com.datapath.datasourceapp.request.SearchProperty;
import com.datapath.datasourceapp.response.CollectionResponse;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class SearchService {

    private final DaoService daoService;

    public List<Document> search(SearchProperty property) {
        return daoService.searchItem(property)
                .stream()
                .peek(d -> d.remove("_id"))
                .collect(toList());
    }

    public CollectionResponse collections() {
        return new CollectionResponse(daoService.getCollectionInfo());
    }
}
