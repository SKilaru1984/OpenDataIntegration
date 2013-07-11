package gov.nyc.opendata.integration.service;

import gov.nyc.opendata.integration.dto.DataSetMetaDTO;

import com.socrata.api.HttpLowLevel;


public interface SocrataCreateService {
	public String createDataSet(DataSetMetaDTO dataSetMetaDTO, HttpLowLevel httpLowLevel)
			throws Exception;
}
