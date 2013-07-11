package gov.nyc.opendata.integration.service;

import gov.nyc.opendata.integration.dao.JdbcServiceRequestDAO;

import gov.nyc.opendata.integration.process.SocrataAppendProcess;
import gov.nyc.opendata.integration.process.SocrataProcess;
import gov.nyc.opendata.integration.process.SocrataReplaceProcess;
import gov.nyc.opendata.integration.process.SocrataUpsertProcess;
import gov.nyc.opendata.integration.util.PropertyLoaderUtil;
import gov.nyc.opendata.integration.util.SendMail;
import gov.nyc.opendata.integration.util.SplitFiles;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.socrata.api.HttpLowLevel;

/**
 * This class simply provides an example to append, replace or upsert to an
 * existing dataset.
 */
public class SocrataServiceImpl implements SocrataService {

	private final String datasetId;

	private final HttpLowLevel httpLowLevel;
	private final String dataSetName;
	private static final Logger logger = Logger
			.getLogger(SocrataServiceImpl.class);

	public SocrataServiceImpl(String datasetId, HttpLowLevel httpLowLevel,
			String dataSetName) {

		this.datasetId = datasetId;
		this.httpLowLevel = httpLowLevel;
		this.dataSetName = dataSetName;

	}

	/**
	 * Replace or Append or Upsert to datasets in Socrata.
	 * 
	 * @param flag
	 *            Replace (R) or Upsert (U) or Append (A)
	 * 
	 * @param dataSetPath
	 *            location to the datafile
	 */

	public void process(String flag, String dataSetPath) throws Exception {

		logger.info("In process for append, replace and upsert ");
		ArrayList<File> files = new ArrayList<File>();

		logger.info("Split the file " + dataSetPath);

		// Split the files in case of large files
		files = SplitFiles.getDataFiles(dataSetPath,
				PropertyLoaderUtil.PUBLISH_THRESHOLD);

		logger.info("No of split files: " + files.size());

		SocrataProcess socrataProcess = null;

		if (flag.equalsIgnoreCase(PropertyLoaderUtil.REPLACE_FLAG)) {

			socrataProcess = new SocrataReplaceProcess();

		} else if (flag.equalsIgnoreCase(PropertyLoaderUtil.APPEND_FLAG)) {

			socrataProcess = new SocrataAppendProcess();

		} else if (flag.equalsIgnoreCase(PropertyLoaderUtil.UPSERT_FLAG)) {

			socrataProcess = new SocrataUpsertProcess();
		}

		if (socrataProcess != null)
			socrataProcess.execute(files, httpLowLevel, datasetId);

		logger.info("View published successfully for the dataset "
				+ dataSetName);

		updateStatusAndDeleteFile();

	}

	/**
	 * Delete successfully published files.
	 * 
	 * @param statusFlag
	 *            JAVAFAIL or PUBLISHED
	 * 
	 * @return success or failure
	 */
	public boolean updateStatusAndDeleteFile() {

		String dataFilePath;

		dataFilePath = JdbcServiceRequestDAO.updateDataSetStatus(
				PropertyLoaderUtil.JAVA_PUBLISHED, dataSetName);

		boolean success = false;

		// Delete the published file

		File f = new File(dataFilePath);
		success = f.delete();

		if (!success) {

			// Send failure notification in case the file could not be
			// deleted

			logger.info("Failed to delete the file " + dataFilePath);
			SendMail.sendMail(PropertyLoaderUtil.MAIL_SUPPORT,
					PropertyLoaderUtil.MAIL_DELETE_FILE_FAILURE_SUBJECT,
					dataSetName + "&" + dataFilePath,
					PropertyLoaderUtil.MAIL_DELETE_FILE_FAILURE_BODY);
		} else {

			// Send Success notification to limited recipients

			SendMail.sendMail(
					PropertyLoaderUtil.MAIL_PUBLISH_SUCCESS_RECIPIENT,
					PropertyLoaderUtil.MAIL_PUBLSIH_SUCCESS_SUBJECT,
					dataSetName, PropertyLoaderUtil.MAIL_PUBLISH_SUCCESS_BODY);
			logger.info("Successfully deleted  " + dataFilePath);
		}

		return success;
	}

	/**
	 * Updates the datasetid to the database and gets the path to the datafile.
	 * 
	 * 
	 * @param statusFlag
	 *            JAVASTART or JAVAFAIL
	 * @param dataSetId
	 *            id of the created dataset
	 * @param updateFlag
	 *            append or replace or upsert
	 */

	public void updateIdAndStatus(String updateFlag) throws Exception {

		String dataFilePath;

		dataFilePath = JdbcServiceRequestDAO.updateDataSetId(datasetId,
				dataSetName, PropertyLoaderUtil.JAVA_START);

		logger.info("dataFilepath " + dataFilePath);

		this.process(updateFlag, dataFilePath);

	}

}
