package gov.nyc.opendata.integration.process;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.socrata.api.HttpLowLevel;
import com.socrata.api.Soda2Producer;
import com.socrata.model.UpsertResult;

public class SocrataUpsertProcess implements SocrataProcess {
	private static final Logger logger = Logger.getLogger(SocrataUpsertProcess.class);
	/**
	 * Upsert data to the existing dataset.
	 * 
	 */
	public void execute(ArrayList<File> files, HttpLowLevel httpLowlevel,
			String datasetId) throws Exception {

		Soda2Producer soda2Producer = new Soda2Producer(httpLowlevel);
		
		boolean deleteFile = false;
		
		for (File file : files) {
			logger.info("In upsert for  " + file.getName());
			UpsertResult upsertResult = soda2Producer
					.upsertCsv(datasetId, file);
			StringBuilder sb = new StringBuilder();
			sb.append("Updates:").append(upsertResult.getRowsUpdated())
					.append("; ");
			sb.append("Creates:").append(upsertResult.getRowsCreated())
					.append("; ");
			sb.append("Deleted:").append(upsertResult.getRowsDeleted())
					.append("; ");
			sb.append("Errors:").append(upsertResult.getErrors());
			logger.info(sb.toString());
			
			
			if(files.size() > 1)
			deleteFile =  deleteFile(file);
			
			logger.info("File deleted " + deleteFile);
		}
	}

	public boolean deleteFile(File f) {
		boolean success = f.delete();

		return success;
	}
}
