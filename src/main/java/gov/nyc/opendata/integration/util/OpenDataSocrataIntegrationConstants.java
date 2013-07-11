package gov.nyc.opendata.integration.util;

public interface OpenDataSocrataIntegrationConstants {




//	public static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
	public static final String GET_DATASET_LIST = "{call nyc_open_data_pack.get_generated_datasets(?)}";
	public static final String UPDATE_DATASET_FLAG_GET_PATH = "{call nyc_open_data_pack.get_dataset_Path(?,?,?)}";
	public static final String UPDATE_DATASET_FLAG = "{call nyc_open_data_pack.update_publish_status(?,?,?)}";
	public static final String UPDATE_DATASET_ID = "{call nyc_open_data_pack.update_dataset_id(?,?,?,?)}";
	

}
