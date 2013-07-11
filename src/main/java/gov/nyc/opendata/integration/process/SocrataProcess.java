package gov.nyc.opendata.integration.process;

import java.io.File;
import java.util.ArrayList;

import com.socrata.api.HttpLowLevel;

public interface SocrataProcess {

	/**
	 * 
	 * 
	 * @param files
	 *            list of files to be processed
	 *            
	 * @param httpLowlevel
	 * 			  JDBC Importer with all the connection information needed for connecting to the
     *			  database as well as Socrata
     *
	 * @param datasetId
	 *            id of the dataset
	 *            
	 */
	public void execute(ArrayList<File> files, HttpLowLevel httpLowlevel,
			String datasetId) throws Exception;


	
}
