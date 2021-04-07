package com.datapath.datasourceapp;

import com.datapath.datasourceapp.domain.InitCollectionDomain;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

import static com.datapath.datasourceapp.Constants.*;

@Slf4j
@Component
@AllArgsConstructor
public class DBInitializer implements InitializingBean {

    private final MongoTemplate template;
    private final ObjectMapper mapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("DB initialization started");

        File[] files = files();

        log.info("Found collections {}", files.length);

        for (File file : files) {
            InitCollectionDomain collection = mapper.readValue(file, InitCollectionDomain.class);

            log.info("Processing collection {}", collection.getName());

            saveItems(collection);
            saveCollectionInfo(collection);
        }

        log.info("DB initialization finished");
    }

    private File[] files() throws FileNotFoundException {
        return ResourceUtils.getFile(DB_PATH).listFiles();
    }

    private void saveCollectionInfo(InitCollectionDomain collection) {
        Query query = new Query();
        query.addCriteria(Criteria.where(NAME).is(collection.getName()));

        Document document = new Document();
        document.put(NAME, collection.getName());
        document.put(DESCRIPTION, collection.getDescription());
        document.put(KEY, collection.getKey());
        document.put(FIELDS, collection.getFields());

        template.upsert(query, Update.fromDocument(document), COLLECTIONS);
    }

    private void saveItems(InitCollectionDomain collection) {
        collection.getItems().forEach(item -> updateItem(collection, item));
    }

    private void updateItem(InitCollectionDomain collection, Document item) {
        Query query = new Query();
        query.addCriteria(Criteria.where(collection.getKey()).is(item.get(collection.getKey())));

        template.upsert(query, Update.fromDocument(item), collection.getName());
    }
}
