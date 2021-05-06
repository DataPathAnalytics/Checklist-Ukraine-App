package com.datapath.analyticapp.service.miner.rule;

import com.datapath.analyticapp.service.miner.config.Place;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toCollection;

@Service
public class MinerRuleProvider {

    private static final String RULES_FILE_NAME = "rules/mining_rules.json";
    private final Set<MinerRule> rules;

    public MinerRuleProvider() throws IOException {
        this.rules = new ObjectMapper()
                .readValue(new InputStreamReader(new DefaultResourceLoader().getResource(RULES_FILE_NAME).getInputStream(), UTF_8),
                        new TypeReference<LinkedHashSet<MinerRule>>() {
                        }
                );
    }

    public Set<MinerRule> getRulesByPlace(Place place) {
        return rules.stream()
                .filter(r -> place.equals(r.getPlace()))
                .collect(toCollection(LinkedHashSet::new));
    }
}
