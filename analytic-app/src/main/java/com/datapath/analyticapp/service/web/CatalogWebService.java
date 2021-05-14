package com.datapath.analyticapp.service.web;

import com.datapath.analyticapp.dao.entity.EventTypeEntity;
import com.datapath.analyticapp.dao.entity.KnowledgeClassEntity;
import com.datapath.analyticapp.dao.entity.RoleEntity;
import com.datapath.analyticapp.dao.repository.EventTypeRepository;
import com.datapath.analyticapp.dao.repository.KnowledgeClassRepository;
import com.datapath.analyticapp.dao.repository.RoleRepository;
import com.datapath.analyticapp.dto.EventTypeDTO;
import com.datapath.analyticapp.dto.KnowledgeClassDTO;
import com.datapath.analyticapp.dto.RoleDTO;
import com.datapath.analyticapp.dto.request.SearchRequest;
import com.datapath.analyticapp.dto.response.PageableResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static com.datapath.analyticapp.Constants.SEARCH_PATTERN;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CatalogWebService {

    private final KnowledgeClassRepository knowledgeClassRepository;
    private final EventTypeRepository eventTypeRepository;
    private final RoleRepository roleRepository;

    public PageableResponse<KnowledgeClassDTO> searchKnowledgeClasses(SearchRequest request) {
        Page<KnowledgeClassEntity> page = knowledgeClassRepository.findByKnowledgeClassNameMatchesRegexOrderByKnowledgeClassName(
                String.format(SEARCH_PATTERN, request.getKeyword()), PageRequest.of(request.getPage(), request.getSize()));

        return new PageableResponse<>(
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.get()
                        .map(k -> new KnowledgeClassDTO(k.getId(), k.getKnowledgeClassName()))
                        .collect(toList())
        );
    }

    public PageableResponse<EventTypeDTO> searchEventTypes(SearchRequest request) {
        Page<EventTypeEntity> page = eventTypeRepository.findByEventTypeNameMatchesRegexOrderByEventTypeName(
                String.format(SEARCH_PATTERN, request.getKeyword()), PageRequest.of(request.getPage(), request.getSize()));

        return new PageableResponse<>(
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.get()
                        .map(i -> new EventTypeDTO(i.getId(), i.getEventTypeName()))
                        .collect(toList())
        );
    }

    public PageableResponse<RoleDTO> searchRoles(SearchRequest request) {
        Page<RoleEntity> page = roleRepository.findByRoleNameMatchesRegexOrderByRoleName(
                String.format(SEARCH_PATTERN, request.getKeyword()), PageRequest.of(request.getPage(), request.getSize()));

        return new PageableResponse<>(
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.get()
                        .map(i -> new RoleDTO(i.getId(), i.getRoleName()))
                        .collect(toList())
        );
    }
}
