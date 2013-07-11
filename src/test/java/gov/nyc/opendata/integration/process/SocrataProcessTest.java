package gov.nyc.opendata.integration.process;

import gov.nyc.opendata.integration.dao.ExcelServiceRequestDAO;
import gov.nyc.opendata.integration.dto.DataSetMetaDTO;
import gov.nyc.opendata.integration.service.SocrataCreateService;
import gov.nyc.opendata.integration.service.SocrataCreateServiceImpl;
import gov.nyc.opendata.integration.util.PropertyLoaderUtil;
import gov.nyc.opendata.integration.util.SplitFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import junit.framework.TestCase;

import com.socrata.api.HttpLowLevel;
import com.socrata.api.SodaImporter;

public class SocrataProcessTest extends TestCase {

	String datasetId = null;
	SocrataProcess socrataProcess = null;
	HttpLowLevel httpLowLevel = null;
	ArrayList<File> files = new ArrayList<File>();

	public void setUp() throws Exception {
		super.setUp();

		ExcelServiceRequestDAO excelServiceRequestDAO = new ExcelServiceRequestDAO();

		DataSetMetaDTO dataSetMetaDTO = excelServiceRequestDAO
				.getDataSetMetaData("src/test/resources/Test_DataSet_List.xlsx");

		files = SplitFiles.getDataFiles("src/test/resources/Test_DataFile.tsv",
				PropertyLoaderUtil.PUBLISH_THRESHOLD);

		if (PropertyLoaderUtil.PROXY_ENABLED) {
			// Proxy Settings
			Properties sysProperties = System.getProperties();

			sysProperties.put("https.proxyHost", PropertyLoaderUtil.PROXY_HOST);
			sysProperties.put("https.proxyPort", PropertyLoaderUtil.PROXY_PORT);
			sysProperties.put("http.proxyHost", PropertyLoaderUtil.PROXY_HOST);
			sysProperties.put("http.proxyPort", PropertyLoaderUtil.PROXY_PORT);

			sysProperties.put("proxySet", "true");

		}

		// Creates a JDBC Importer with all the connection
		// information needed for connecting to the Socrata Portal

		httpLowLevel = HttpLowLevel.instantiateBasic(PropertyLoaderUtil.DOMAIN,
				PropertyLoaderUtil.USER, PropertyLoaderUtil.STRING_ENCRYPTER
						.decrypt(PropertyLoaderUtil.PASSWORD),
				PropertyLoaderUtil.APPTOKEN);

		SocrataCreateService socrataCreateService = new SocrataCreateServiceImpl();
		datasetId = socrataCreateService.createDataSet(dataSetMetaDTO,
				httpLowLevel);

	}

	public void testExecute() throws Exception {
		try {

			// Test Append
			socrataProcess = new SocrataAppendProcess();
			socrataProcess.execute(files, httpLowLevel, datasetId);

			// Test Upsert
			socrataProcess = new SocrataUpsertProcess();
			socrataProcess.execute(files, httpLowLevel, datasetId);

			// Test Replace
			socrataProcess = new SocrataReplaceProcess();
			socrataProcess.execute(files, httpLowLevel, datasetId);

		} finally {
			System.out.println("Deleting the dataset for datasetID "
					+ datasetId);
			SodaImporter sodaImporter = new SodaImporter(httpLowLevel);
			sodaImporter.deleteView(datasetId);
		}

	}
}
