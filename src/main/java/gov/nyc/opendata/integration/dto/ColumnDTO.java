package gov.nyc.opendata.integration.dto;
/** 
 * The ColumnDTO class holds all required fields for dataset column
 */
public class ColumnDTO {
	private String columnName;
	private String columnDesc;
	private String columnType;
	


	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnDesc() {
		return columnDesc;
	}

	public void setColumnDesc(String columnDesc) {
		this.columnDesc = columnDesc;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	
}
