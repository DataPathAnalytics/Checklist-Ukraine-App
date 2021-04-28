package com.datapath.analyticapp.service.miner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

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

    public Set<MinerRule> getRules() {
        return rules;
    }
}
