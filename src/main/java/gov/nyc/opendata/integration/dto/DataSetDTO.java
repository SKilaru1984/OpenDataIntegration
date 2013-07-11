package gov.nyc.opendata.integration.dto;
/** 
 * The DataSetDTO class holds all required fields from the control table
 * for processing dataset 
 */
public class DataSetDTO {
	
	private String dataSetId;
	private String dataSetPath;
	private String dataSetName;
	private String flag;
	private String agencyContact;
	private String dataDictionaryPath;

	public String getDataSetName() {
		return dataSetName;
	}

	public void setDataSetName(String dataSetName) {
		this.dataSetName = dataSetName;
	}

	public String getDataSetId() {
		return dataSetId;
	}

	public void setDataSetId(String dataSetId) {
		this.dataSetId = dataSetId;
	}

	public String getDataSetPath() {
		return dataSetPath;
	}

	public void setDataSetPath(String dataSetPath) {
		this.dataSetPath = dataSetPath;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getAgencyContact() {
		return agencyContact;
	}

	public void setAgencyContact(String agencyContact) {
		this.agencyContact = agencyContact;
	}

	public String getDataDictionaryPath() {
		return dataDictionaryPath;
	}

	public void setDataDictionaryPath(String dataDictionaryPath) {
		this.dataDictionaryPath = dataDictionaryPath;
	}
}
