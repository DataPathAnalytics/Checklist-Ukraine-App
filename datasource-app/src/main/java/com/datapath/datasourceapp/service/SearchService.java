package com.datapath.datasourceapp.service;

import com.datapath.datasourceapp.request.SearchProperty;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SearchService {

    private final MongoTemplate template;

    public List<HashMap> search(List<SearchProperty> properties) {
        List<String> datasource = properties
                .stream()
                .map(SearchProperty::getDatasource)
                .distinct()
                .collect(Collectors.toList());

        if (datasource.size() > 1) {
            throw new RuntimeException("Multiple datasource request");
        }

        Query query = new Query();
        query.limit(10);

        properties.forEach(p -> query.addCriteria(Criteria.where(p.getFieldName()).regex("^" + p.getFieldValue())));

        return template.find(query, HashMap.class, datasource.get(0));
    }
}
