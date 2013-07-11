package gov.nyc.opendata.integration.dto;

/**
 * The DataSetIndexDTO class holds all the indexes of required fileds in the
 * datset excel file
 */
public class DataSetIndexDTO {
	private int dataSetTitleIndex = -1;
	private int dataSetDescIndex = -1;
	private int dataSetKeyWordsIndex = -1;
	private int updateFreqIndex = -1;
	private int categoryIndex = -1;
	private int attributionLinkIndex = -1;
	private int dataDictionaryIndex = -1;
	private int attributionIndex = -1;

	
	private int columnNameIndex = -1;
	private int columnDescIndex = -1;
	private int columnTypeIndex = -1;
	
	public int getCategoryIndex() {
		return categoryIndex;
	}

	public void setCategoryIndex(int categoryIndex) {
		this.categoryIndex = categoryIndex;
	}

	public int getDataSetTitleIndex() {
		return dataSetTitleIndex;
	}

	public void setDataSetTitleIndex(int dataSetTitleIndex) {
		this.dataSetTitleIndex = dataSetTitleIndex;
	}

	public int getDataSetDescIndex() {
		return dataSetDescIndex;
	}

	public void setDataSetDescIndex(int dataSetDescIndex) {
		this.dataSetDescIndex = dataSetDescIndex;
	}

	public int getDataSetKeyWordsIndex() {
		return dataSetKeyWordsIndex;
	}

	public void setDataSetKeyWordsIndex(int dataSetKeyWordsIndex) {
		this.dataSetKeyWordsIndex = dataSetKeyWordsIndex;
	}

	public int getUpdateFreqIndex() {
		return updateFreqIndex;
	}

	public void setUpdateFreqIndex(int updateFreqIndex) {
		this.updateFreqIndex = updateFreqIndex;
	}

	public int getAttributionLinkIndex() {
		return attributionLinkIndex;
	}

	public void setAttributionLinkIndex(int attributionLinkIndex) {
		this.attributionLinkIndex = attributionLinkIndex;
	}

	public int getAttributionIndex() {
		return attributionIndex;
	}

	public void setAttributionIndex(int attributionIndex) {
		this.attributionIndex = attributionIndex;
	}

	public int getColumnNameIndex() {
		return columnNameIndex;
	}

	public void setColumnNameIndex(int columnNameIndex) {
		this.columnNameIndex = columnNameIndex;
	}

	public int getColumnDescIndex() {
		return columnDescIndex;
	}

	public void setColumnDescIndex(int columnDescIndex) {
		this.columnDescIndex = columnDescIndex;
	}

	public int getDataDictionaryIndex() {
		return dataDictionaryIndex;
	}

	public void setDataDictionaryIndex(int dataDictionaryIndex) {
		this.dataDictionaryIndex = dataDictionaryIndex;
	}

	public int getColumnTypeIndex() {
		return columnTypeIndex;
	}

	public void setColumnTypeIndex(int columnTypeIndex) {
		this.columnTypeIndex = columnTypeIndex;
	}

}
