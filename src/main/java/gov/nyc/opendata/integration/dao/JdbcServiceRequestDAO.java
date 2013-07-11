package gov.nyc.opendata.integration.dao;

import gov.nyc.opendata.integration.dto.DataSetDTO;
import gov.nyc.opendata.integration.util.OpenDataSocrataIntegrationConstants;
import gov.nyc.opendata.integration.util.PropertyLoaderUtil;
import gov.nyc.opendata.integration.util.SendMail;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * The JdbcServiceRequestDAO class is mainly for CRUD operations on database.
 */

public class JdbcServiceRequestDAO implements
		OpenDataSocrataIntegrationConstants {

	private static final DriverManagerDataSource dataSource = initDataSetIdDAO();

	private static final Logger logger = Logger
			.getLogger(JdbcServiceRequestDAO.class);

	/**
	 * Get the list of all the generated datasets from the control table.
	 * 
	 * 
	 * @return ArrayList<DataSetDTO> list of generated datasets
	 * 
	 */
	public static ArrayList<DataSetDTO> getDataSetList() {

		Connection conn = null;

		ArrayList<DataSetDTO> dsDTOList = new ArrayList<DataSetDTO>();
		try {

			conn = dataSource.getConnection();

			CallableStatement cstmt = conn.prepareCall(GET_DATASET_LIST);
			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			cstmt.execute();

			ResultSet rs = (ResultSet) cstmt.getObject(1);
			while (rs.next()) {
				DataSetDTO dsDTO = new DataSetDTO();
				dsDTO.setDataSetId(rs.getString("datasetId"));
				dsDTO.setDataSetPath(rs.getString("dataFile"));
				dsDTO.setDataSetName(rs.getString("datasetName"));
				dsDTO.setFlag(rs.getString("flag"));
				dsDTO.setAgencyContact(rs.getString("agencyContact"));
				dsDTO.setDataDictionaryPath(rs.getString("dataDictionary"));
				dsDTOList.add(dsDTO);
			}

		} catch (SQLException e) {
			logger.error(e);
			SendMail.sendMail(PropertyLoaderUtil.MAIL_SUPPORT,
					PropertyLoaderUtil.MAIL_DB_FAILURE_SUBJECT, null,
					PropertyLoaderUtil.MAIL_DB_FAILURE_BODY);
		} finally {

			if (conn != null) {

				try {
					conn.close();
				} catch (SQLException e) {

					logger.error(e);
					SendMail.sendMail(PropertyLoaderUtil.MAIL_SUPPORT,
							PropertyLoaderUtil.MAIL_DB_FAILURE_SUBJECT, null,
							PropertyLoaderUtil.MAIL_DB_FAILURE_BODY);
				}

			}
		}

		return dsDTOList;
	}

	// Initialize the datasource
	private static DriverManagerDataSource initDataSetIdDAO() {

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource
				.setDriverClassName(PropertyLoaderUtil.DATASOURCE_DRIVER_NAME);
		dataSource.setUrl(PropertyLoaderUtil.DATASOURCE_URL);
		dataSource.setUsername(PropertyLoaderUtil.DATASOURCE_USERNAME);

		dataSource.setPassword(PropertyLoaderUtil.DB_DECRYTED_PASSWORD);

		return dataSource;
	}

	/**
	 * Update the control table with the status flag (JAVAFAIL or PUBLISHED).
	 * 
	 * @param updateFlag
	 *            JAVAFAIL or PUBLISHED
	 * 
	 * @param dataSetName
	 *            name of the dataset
	 * 
	 * @return dataFilePath location of the data file.
	 * 
	 */
	public static String updateDataSetStatus(String updateFlag,
			String dataSetName) {
		Connection conn = null;
		String dataSetPath = "";

		CallableStatement cstmt;
		String subject;
		String body;
		if (updateFlag.equalsIgnoreCase(PropertyLoaderUtil.JAVA_PUBLISHED)) {
			subject = PropertyLoaderUtil.MAIL_DB_UPDATE_FAIL_AFTER_PUBLISH_SUCCESS_SUBJECT;
			body = PropertyLoaderUtil.MAIL_DB_UPDATE_FAIL_AFTER_PUBLISH_SUCCESS_BODY;
		} else {
			subject = PropertyLoaderUtil.MAIL_DB_UPDATE_FAIL_AFTER_PUBLISH_FAIL_SUBJECT;
			body = PropertyLoaderUtil.MAIL_DB_UPDATE_FAIL_AFTER_PUBLISH_FAIL_BODY;
		}
		try {

			conn = dataSource.getConnection();
			if (updateFlag.equalsIgnoreCase(PropertyLoaderUtil.JAVA_PUBLISHED)) {
				cstmt = conn.prepareCall(UPDATE_DATASET_FLAG_GET_PATH);

			} else {
				cstmt = conn.prepareCall(UPDATE_DATASET_FLAG);

			}

			cstmt.setString(1, dataSetName);
			cstmt.setString(2, updateFlag);
			cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
			cstmt.execute();
			dataSetPath = cstmt.getString(3);

		} catch (SQLException e) {
			logger.error(e);

			SendMail.sendMail(PropertyLoaderUtil.MAIL_SUPPORT, subject,
					dataSetName, body);

		} finally {

			if (conn != null) {

				try {
					conn.close();
				} catch (SQLException e) {

					SendMail.sendMail(PropertyLoaderUtil.MAIL_SUPPORT, subject,
							dataSetName, body);

				}

			}
		}
		return dataSetPath;
	}

	/**
	 * Update the control table with the dataset id.
	 * 
	 * @param dataSetId
	 *            dataset id for the created dataset
	 * 
	 * @param dataSetName
	 *            name of the dataset
	 * 
	 * @param statusFlag
	 *            JAVAFAIL or JAVASTART
	 * 
	 * @return dataFilepath location of the data file.
	 * 
	 */
	public static String updateDataSetId(String dataSetId, String dataSetName,
			String statusFlag) {
		Connection conn = null;
		String dataFilePath = "";

		String subject = "";
		String body = "";

		try {

			conn = dataSource.getConnection();

			CallableStatement cstmt = conn.prepareCall(UPDATE_DATASET_ID);

			cstmt.setString(1, dataSetName);
			cstmt.setString(2, dataSetId);
			cstmt.setString(3, statusFlag);
			cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
			cstmt.execute();
			dataFilePath = cstmt.getString(4);
			logger.info("DB return dataSetPath " + dataFilePath);

		} catch (SQLException e) {
			logger.error(e);

			SendMail.sendMail(PropertyLoaderUtil.MAIL_SUPPORT, subject,
					dataSetName, body);

		} finally {

			if (conn != null) {

				try {
					conn.close();
				} catch (SQLException e) {

					SendMail.sendMail(PropertyLoaderUtil.MAIL_SUPPORT, subject,
							dataSetName, body);

				}

			}
		}
		return dataFilePath;
	}
}
