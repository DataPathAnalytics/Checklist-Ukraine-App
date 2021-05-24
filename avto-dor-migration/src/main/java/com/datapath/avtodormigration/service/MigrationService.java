package com.datapath.avtodormigration.service;

import com.datapath.avtodormigration.dao.entity.ChecklistEntity;
import com.datapath.avtodormigration.dao.entity.ContractEntity;
import com.datapath.avtodormigration.dao.repository.ChecklistRepository;
import com.datapath.avtodormigration.dao.repository.ContractRepository;
import com.datapath.avtodormigration.dto.request.ControlActivityRequest;
import com.datapath.avtodormigration.dto.request.ResponseSessionRequest;
import com.datapath.avtodormigration.service.builder.BuilderProvider;
import com.datapath.avtodormigration.service.builder.ControlActivityBuilder;
import com.datapath.avtodormigration.service.builder.ResponseSessionBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Service
@AllArgsConstructor
public class MigrationService {

    private final MigrationHistoryService historyService;
    private final UploadDataService uploadDataService;
    private final ChecklistRepository checklistRepository;
    private final ContractRepository contractRepository;
    private final BuilderProvider builderProvider;

    @Transactional
    public void doMigrate() {
        List<ChecklistEntity> all = checklistRepository.findForMigrate();

        if (isEmpty(all)) return;

        log.info("Found {} checklists for migration", all.size());

        all.forEach(this::doMigrate);

        log.info("Migration completed");
    }

    private void doMigrate(ChecklistEntity checklist) {
        log.info("Process checklist {}", checklist.getId());

        Optional<ContractEntity> byId = contractRepository.findById(checklist.getContractId());
        if (!byId.isPresent()) {
            log.warn("Not found contract {}", checklist.getContractId());
            return;
        }

        ContractEntity contract = byId.get();

        if (historyService.existsByContractIdentifier(contract.getContractId())) {
            log.info("Contract {} already migrated", checklist.getContractId());
            return;
        }

        handleResponseSession(checklist, contract, handleControlActivity(checklist));
    }

    private void handleResponseSession(ChecklistEntity checklist, ContractEntity contract, Integer controlActivityId) {
        ResponseSessionBuilder builder = builderProvider.getResponseSessionBuilder(checklist);

        ResponseSessionRequest request = builder.prepareToMigration(checklist, contract, controlActivityId);

        Integer savedId = uploadDataService.uploadResponseSession(request);
        log.info("Uploaded response session. Id {}", savedId);

        historyService.addContractResponseSession(contract.getContractId(), savedId);
    }

    private Integer handleControlActivity(ChecklistEntity checklist) {
        ControlActivityBuilder builder = builderProvider.getControlActivityBuilder(checklist);

        ControlActivityRequest request = builder.prepareToMigration(checklist);

        Integer savedId = uploadDataService.uploadControlActivity(request);
        log.info("Uploaded control activity. Id {}", savedId);

        return savedId;
    }
}
