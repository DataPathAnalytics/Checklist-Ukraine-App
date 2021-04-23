package com.datapath.analyticapp.service.miner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

@Service
public class MinerRuleProvider {

    private static final String RULES_FILE_NAME = "rules/mining_rules.json";
    private final List<MinerRule> rules;

    public MinerRuleProvider() throws IOException {
        this.rules = new ObjectMapper()
                .readValue(new InputStreamReader(new DefaultResourceLoader().getResource(RULES_FILE_NAME).getInputStream(), UTF_8),
                        new TypeReference<List<MinerRule>>() {
                        }
                );
    }

    public List<MinerRule> getByRole(String role) {
        return rules.stream()
                .filter(r -> r.getRole().equals(role))
                .collect(toList());
    }
}
