package com.datapath.datasourceapp.service;

import com.datapath.datasourceapp.request.SearchProperty;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class SearchService {

    private final MongoTemplate template;

    public List<Document> search(List<SearchProperty> properties) {
        List<String> datasource = properties
                .stream()
                .map(SearchProperty::getDatasource)
                .distinct()
                .collect(toList());

        if (datasource.size() > 1) {
            throw new RuntimeException("Multiple datasource request");
        }

        Query query = new Query();
        query.limit(10);

        properties.forEach(p -> query.addCriteria(Criteria.where(p.getFieldName()).regex("^" + p.getFieldValue())));

        return template.find(query, Document.class, datasource.get(0))
                .stream()
                .peek(d -> d.remove("_id"))
                .collect(toList());
    }
}
