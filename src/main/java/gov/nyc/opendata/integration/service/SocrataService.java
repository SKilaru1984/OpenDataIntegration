package gov.nyc.opendata.integration.service;

public interface SocrataService {

	public void process(String flag, String dataSetPath) throws Exception;


	public void updateIdAndStatus(String updateFlag) throws Exception;

}
