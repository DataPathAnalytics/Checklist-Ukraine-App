package com.datapath.avtodormigration.dao.service;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DaoService {

    private static final String CPV_CODE_QUERY = "select cpv2 from contract_item where contract_id = ? limit 1";
    private static final String CPV_NAME_QUERY = "select name from cpv_catalogue where cpv = ? limit 1";

    private final JdbcTemplate template;

    public String getCpvCodeByContractId(Integer contractId) {
        return template.queryForObject(CPV_CODE_QUERY, String.class, contractId);
    }

    public String getCpvNameByCpvCode(String cpvCode) {
        return template.queryForObject(CPV_NAME_QUERY, String.class, cpvCode);
    }
}
