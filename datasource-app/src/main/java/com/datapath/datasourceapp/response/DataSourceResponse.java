package com.datapath.datasourceapp.response;

import com.datapath.datasourceapp.domain.DataSourceInfoDomain;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DataSourceResponse {
    List<DataSourceInfoDomain> dataSources;
}
