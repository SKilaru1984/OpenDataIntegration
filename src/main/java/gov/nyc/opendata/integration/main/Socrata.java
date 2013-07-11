package gov.nyc.opendata.integration.main;

import org.apache.log4j.Logger;

import com.socrata.api.HttpLowLevel;

import gov.nyc.opendata.integration.util.OpenDataSocrataIntegrationConstants;
import gov.nyc.opendata.integration.util.PropertyLoaderUtil;
import gov.nyc.opendata.integration.util.SendMail;
import gov.nyc.opendata.integration.dao.ExcelServiceRequestDAO;
import gov.nyc.opendata.integration.dao.JdbcServiceRequestDAO;
import gov.nyc.opendata.integration.dto.DataSetDTO;
import gov.nyc.opendata.integration.dto.DataSetMetaDTO;
import gov.nyc.opendata.integration.service.SocrataCreateService;
import gov.nyc.opendata.integration.service.SocrataCreateServiceImpl;
import gov.nyc.opendata.integration.service.SocrataService;
import gov.nyc.opendata.integration.service.SocrataServiceImpl;

/**
 * The Socrata class checks for all the datasets flags (Upsert, Replace or
 * Append) and processes these datasets accordingly
 */
 class Socrata implements OpenDataSocrataIntegrationConstants, Runnable {

	private final HttpLowLevel httpLowLevel;
	private final DataSetDTO dataSetDTO;
	private static final Logger logger = Logger.getLogger(Socrata.class);

	 Socrata(DataSetDTO dataSetDTO, HttpLowLevel httpLowLevel) {
		this.dataSetDTO = dataSetDTO;
		this.httpLowLevel = httpLowLevel;

	}

	public void run() {

		SocrataService service = null;

		try {

			if (dataSetDTO.getDataSetId() != null
					&& dataSetDTO.getFlag() != null) {

				service = new SocrataServiceImpl(dataSetDTO.getDataSetId(),
						httpLowLevel, dataSetDTO.getDataSetName());

				logger.info("In Append or Replace or Upsert for "
						+ dataSetDTO.getDataSetId());

				// Call ServiceImpl replace or append method
				service.process(dataSetDTO.getFlag(),
						dataSetDTO.getDataSetPath());

			}

			else {

				logger.info("In create for " + dataSetDTO.getDataSetName());

				ExcelServiceRequestDAO excelServiceRequestDAO = new ExcelServiceRequestDAO();

				DataSetMetaDTO dataSetMetaDTO = excelServiceRequestDAO
						.getDataSetMetaData(dataSetDTO.getDataDictionaryPath());

				// Create the dataSet in Socrata
				SocrataCreateService socrataCreateService = new SocrataCreateServiceImpl();
				String datasetId = socrataCreateService.createDataSet(
						dataSetMetaDTO, httpLowLevel);

				service = new SocrataServiceImpl(datasetId, httpLowLevel,
						dataSetDTO.getDataSetName());

				// update datasetId to db and then process the data file
				service.updateIdAndStatus(dataSetDTO.getFlag());

			}

		} catch (Exception e) {

			logger.error("Exception while publishing", e);

			String dbReturnValue = JdbcServiceRequestDAO.updateDataSetStatus(
					PropertyLoaderUtil.JAVA_FAIL, dataSetDTO.getDataSetName());
			logger.info("dbReturnValue " + dbReturnValue);

			// Send failure notification to support team and agency
			SendMail.sendMail(dataSetDTO.getAgencyContact() + ","
					+ PropertyLoaderUtil.MAIL_SUPPORT,
					PropertyLoaderUtil.MAIL_PUBLISH_FAILURE_SUBJECT,
					dataSetDTO.getDataSetName(),
					PropertyLoaderUtil.MAIL_PUBLISH_FAILURE_BODY);

		}

	}

}
