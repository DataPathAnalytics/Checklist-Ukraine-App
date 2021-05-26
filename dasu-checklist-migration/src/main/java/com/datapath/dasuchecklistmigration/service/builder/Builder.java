package com.datapath.dasuchecklistmigration.service.builder;

import com.datapath.dasuchecklistmigration.service.TemplateProvideService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.util.Objects.isNull;

public interface Builder {

    String PATTERN = "yyyy-MM-dd";

    Integer forTemplateType();

    TemplateProvideService getTemplateProvideService();

    default String asString(LocalDate date) {
        if (isNull(date)) return null;
        return date.format(DateTimeFormatter.ofPattern(PATTERN));
    }

    default String asString(LocalDateTime date) {
        if (isNull(date)) return null;
        return date.format(DateTimeFormatter.ofPattern(PATTERN));
    }
}
