package gov.nyc.opendata.integration.dto;

import java.util.ArrayList;

/**
 * The DataSetMetaDTO represents the metadata of the dataset
 */
public class DataSetMetaDTO {

	private String dataSetName;
	private String dataSetDesc;
	private String updateFreq;
	private String keywords;

	private String category;
	private String agencyName;
	private String attributionLink;
	private ArrayList<ColumnDTO> columns;

	public String getAttributionLink() {
		return attributionLink;
	}

	public void setAttributionLink(String attributionLink) {
		this.attributionLink = attributionLink;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDataSetName() {
		return dataSetName;
	}

	public void setDataSetName(String dataSetName) {
		this.dataSetName = dataSetName;
	}

	public String getDataSetDesc() {
		return dataSetDesc;
	}

	public void setDataSetDesc(String dataSetDesc) {
		this.dataSetDesc = dataSetDesc;
	}

	public String getUpdateFreq() {
		return updateFreq;
	}

	public void setUpdateFreq(String updateFreq) {
		this.updateFreq = updateFreq;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public ArrayList<ColumnDTO> getColumns() {
		return columns;
	}

	public void setColumns(ArrayList<ColumnDTO> columns) {
		this.columns = columns;
	}

}