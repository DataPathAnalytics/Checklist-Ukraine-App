package com.datapath.datasourceapp.response;

import com.datapath.datasourceapp.domain.CollectionInfoDomain;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CollectionResponse {
    List<CollectionInfoDomain> collections;
}
