package com.datapath.datasourceapp.service;

import com.datapath.datasourceapp.domain.CollectionInfoDomain;
import com.datapath.datasourceapp.domain.InitCollectionDomain;
import com.datapath.datasourceapp.request.SearchProperty;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.datapath.datasourceapp.Constants.*;
import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class DaoService {

    private final MongoTemplate template;

    public void saveItem(InitCollectionDomain collection, Document item) {
        Query query = new Query();
        query.addCriteria(Criteria.where(collection.getKey()).is(item.get(collection.getKey())));

        template.upsert(query, Update.fromDocument(item), collection.getName());
    }

    public void saveCollectionInfo(InitCollectionDomain collection) {
        Query query = new Query();
        query.addCriteria(Criteria.where(NAME).is(collection.getName()));

        Document document = new Document();
        document.put(NAME, collection.getName());
        document.put(DESCRIPTION, collection.getDescription());
        document.put(KEY, collection.getKey());
        document.put(FIELDS, collection.getFields());

        template.upsert(query, Update.fromDocument(document), COLLECTIONS);
    }

    public List<Document> searchItem(SearchProperty property) {
        Query query = new Query();
        query.limit(10);

        if (nonNull(property.getFieldName())) {
            query.with(Sort.by(property.getFieldName()));
            query.addCriteria(Criteria.where(property.getFieldName()).regex("^" + property.getFieldValue()));
        } else {
            query.with(Sort.by("_id"));
        }

        if (nonNull(property.getFilterFieldName())) {
            query.addCriteria(Criteria.where(property.getFilterFieldName()).is(property.getFilterFieldValue()));
        }

        return template.find(query, Document.class, property.getDatasource());
    }

    public List<CollectionInfoDomain> getCollectionInfo() {
        Query query = new Query();
        query.with(Sort.by(NAME));

        return template.find(query, CollectionInfoDomain.class, COLLECTIONS);
    }
}
