package com.datapath.datasourceapp.service;

import com.datapath.datasourceapp.domain.CollectionInfoDomain;
import com.datapath.datasourceapp.request.SearchProperty;
import com.datapath.datasourceapp.response.CollectionResponse;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.datapath.datasourceapp.Constants.COLLECTIONS;
import static com.datapath.datasourceapp.Constants.NAME;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class SearchService {

    private final MongoTemplate template;

    public List<Document> search(SearchProperty property) {
        Query query = new Query();
        query.limit(10);
        query.with(Sort.by(property.getFieldName()));

        query.addCriteria(Criteria.where(property.getFieldName()).regex("^" + property.getFieldValue()));

        if (nonNull(property.getFilterFieldName())) {
            query.addCriteria(Criteria.where(property.getFilterFieldName()).is(property.getFilterFieldValue()));
        }

        return template.find(query, Document.class, property.getDatasource())
                .stream()
                .peek(d -> d.remove("_id"))
                .collect(toList());
    }

    public CollectionResponse collections() {
        Query query = new Query();
        query.with(Sort.by(NAME));

        List<CollectionInfoDomain> collections = template.find(query, CollectionInfoDomain.class, COLLECTIONS);
        return new CollectionResponse(collections);
    }
}
