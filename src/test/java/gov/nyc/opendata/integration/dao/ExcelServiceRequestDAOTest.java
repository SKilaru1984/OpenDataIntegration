package gov.nyc.opendata.integration.dao;


import gov.nyc.opendata.integration.dto.DataSetMetaDTO;
import junit.framework.TestCase;

public class ExcelServiceRequestDAOTest extends TestCase{
	ExcelServiceRequestDAO excelServiceRequestDAO = null;

	public void setUp() throws Exception {
		super.setUp();

		excelServiceRequestDAO = new ExcelServiceRequestDAO();
		
	}
	
	public void testGetDataSetMetaData() throws Exception{ 
		
		DataSetMetaDTO dataSetMetaDTO = excelServiceRequestDAO.getDataSetMetaData("src/test/resources/Test_DataSet_List.xlsx");
		
		assertEquals(dataSetMetaDTO.getAgencyName(), "Transportation Agency");
		assertTrue(dataSetMetaDTO.getColumns().size() > 1);
	}
}
