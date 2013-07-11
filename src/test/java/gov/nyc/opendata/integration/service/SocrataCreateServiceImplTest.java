package gov.nyc.opendata.integration.service;

import java.util.Properties;

import com.socrata.api.HttpLowLevel;
import com.socrata.api.SodaImporter;

import junit.framework.TestCase;
import gov.nyc.opendata.integration.dao.ExcelServiceRequestDAO;
import gov.nyc.opendata.integration.dto.DataSetMetaDTO;
import gov.nyc.opendata.integration.util.PropertyLoaderUtil;

public class SocrataCreateServiceImplTest extends TestCase {
	ExcelServiceRequestDAO excelServiceRequestDAO = null;
	DataSetMetaDTO dataSetMetaDTO = null;
	HttpLowLevel httpLowLevel = null;
	SocrataCreateService socrataCreateService = null;

	public void setUp() throws Exception {
		super.setUp();

		excelServiceRequestDAO = new ExcelServiceRequestDAO();

		dataSetMetaDTO = excelServiceRequestDAO
				.getDataSetMetaData("src/test/resources/Test_DataSet_List.xlsx");

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

		socrataCreateService = new SocrataCreateServiceImpl();

	}

	public void testCreateDataSet() throws Exception {
		String dataSetId = null;
		try {
			dataSetId = socrataCreateService.createDataSet(dataSetMetaDTO,
					httpLowLevel);
			System.out.println("dataSetId " + dataSetId);
			assertNotNull(dataSetId);
		} finally {
			System.out.println("Deleting the dataset for datasetID " + dataSetId);
			SodaImporter sodaImporter = new SodaImporter(httpLowLevel);
			sodaImporter.deleteView(dataSetId);
		}

	}
}
