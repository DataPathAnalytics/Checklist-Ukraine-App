package com.datapath.dasuchecklistmigration;

import com.datapath.dasuchecklistmigration.service.MigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AvtoDorMigrationApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AvtoDorMigrationApplication.class, args);
    }

    @Autowired
    private MigrationService service;

    @Override
    public void run(String... args) {
        service.doMigrate();
    }
}
