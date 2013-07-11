package gov.nyc.opendata.integration.dao;

import java.util.ArrayList;

import gov.nyc.opendata.integration.dto.DataSetDTO;

import junit.framework.TestCase;

public class JdbcServiceRequestDAOTest extends TestCase {

	public ArrayList<DataSetDTO> dsDTOList = null;
	public String dataSetPath = "TestFilePath";

	public void setUp() throws Exception {
		super.setUp();
		dsDTOList = new ArrayList<DataSetDTO>();
	}

	public void testGetDataList() {
		dsDTOList = JdbcServiceRequestDAO.getDataSetList();
		assertNotNull(dsDTOList);
	}

	public void testUpdateDataSetStatus() {
		String dataSetPathLocal = JdbcServiceRequestDAO.updateDataSetStatus(
				"PUBLISHED", "Test_DataSet");
		System.out.println(dataSetPathLocal);
		assertNotNull(dataSetPathLocal);
		assertEquals(dataSetPath, dataSetPathLocal);
	}

	public void testUpdateDataSetId() {
		String dataSetPathLocal = JdbcServiceRequestDAO.updateDataSetId(
				"TEST_ID", "Test_DataSet", "PUBLISHED");
		System.out.println(dataSetPathLocal);
		assertNotNull(dataSetPathLocal);
		assertEquals(dataSetPath, dataSetPathLocal);
	}
}
