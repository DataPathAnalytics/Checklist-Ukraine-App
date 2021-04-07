package com.datapath.datasourceapp;

import com.datapath.datasourceapp.domain.InitCollectionDomain;
import com.datapath.datasourceapp.service.DaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

import static com.datapath.datasourceapp.Constants.DB_PATH;

@Slf4j
@Component
@AllArgsConstructor
public class DBInitializer implements InitializingBean {

    private final DaoService daoService;
    private final ObjectMapper mapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("DB initialization started");

        File[] files = files();

        log.info("Found collections {}", files.length);

        for (File file : files) {
            InitCollectionDomain collection = mapper.readValue(file, InitCollectionDomain.class);

            log.info("Processing collection {}", collection.getName());

            collection.getItems().forEach(item -> daoService.saveItem(collection, item));
            daoService.saveCollectionInfo(collection);
        }

        log.info("DB initialization finished");
    }

    private File[] files() throws FileNotFoundException {
        return ResourceUtils.getFile(DB_PATH).listFiles();
    }
}
