package gov.nyc.opendata.integration.process;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.socrata.api.HttpLowLevel;
import com.socrata.api.SodaImporter;
import com.socrata.exceptions.SodaError;
import com.socrata.model.importer.DatasetInfo;
/**
 * Append data to the existing dataset.
 * 
 */
public class SocrataAppendProcess implements SocrataProcess {

	private static final Logger logger = Logger.getLogger(SocrataAppendProcess.class);

	public void execute(ArrayList<File> files, HttpLowLevel httpLowLevel,
			String datasetId) throws Exception {
		SodaImporter sodaImporter = new SodaImporter(httpLowLevel);
		DatasetInfo datasetInfo = sodaImporter.createWorkingCopy(datasetId);

		logger.info("datasetId for working copy in append "
				+ datasetInfo.getId());

		boolean deleteFile = false;
		try {
			for (File file : files) {
				
				datasetInfo = sodaImporter.append(datasetInfo.getId(), file, 0,
						null);

				logger.info("append process for file " + file.getName());

				if(files.size() > 1){
				deleteFile = deleteFile(file);
				}

				logger.info("File deleted " + deleteFile);
			}

			sodaImporter.publish(datasetInfo.getId());

		} catch (Exception e) {

			deleteOnFailure(sodaImporter, datasetInfo.getId());

			throw e;
		}

	}

	public boolean deleteFile(File f) { 
		
		boolean success = f.delete();

		return success;
	}

	
	/**
	 * delete working copy in case of failure.
	 * 
	 */
	private void deleteOnFailure(SodaImporter sodaImporter, String datasetInfoId)
			throws SodaError, InterruptedException {
		sodaImporter.deleteView(datasetInfoId);
	}
}
