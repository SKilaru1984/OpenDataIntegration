package gov.nyc.opendata.integration.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import gov.nyc.opendata.integration.dto.DataSetMetaDTO;
import gov.nyc.opendata.integration.util.PropertyLoaderUtil;

import com.socrata.api.HttpLowLevel;
import com.socrata.api.SodaImporter;
import com.socrata.builders.DatasetBuilder;
import com.socrata.model.importer.Column;
import com.socrata.model.importer.Dataset;
import com.socrata.model.importer.DatasetInfo;
import com.socrata.model.importer.Metadata;

public class SocrataCreateServiceImpl implements SocrataCreateService {
	

	private static final Logger logger = Logger.getLogger(SocrataCreateServiceImpl.class);
	/**
	 * Creates a dataset.
	 * 
	 * 
	 * @param dataSetMetaDTO
	 *            metadata of the dataset to be created
	 *            
	 *  @param httpLowLevel
	 *             JDBC Importer with all the connection information needed for connecting to the
     *			  database as well as Socrata
	 *            
	 */
	public String createDataSet(DataSetMetaDTO dataSetMetaDTO,HttpLowLevel httpLowLevel)
			throws Exception {

		SodaImporter sodaImporter = new SodaImporter(httpLowLevel);

		Dataset dataset = sodaImporter.createView(generateBuilder(
				dataSetMetaDTO).build());

		DatasetInfo datasetInfo = sodaImporter.publish(dataset.getId());

		return datasetInfo.getId();

	}
	
	private DatasetBuilder generateBuilder(DataSetMetaDTO dataSetMetaDTO) throws Exception{ 

		Metadata metadata = new Metadata();

		metadata.addCustomField("Update", "Update Frequency",
				dataSetMetaDTO.getUpdateFreq());

		Map<String, Object> privateMetadata = new HashMap<String, Object>();

		privateMetadata.put("contactEmail", PropertyLoaderUtil.USER);

		ArrayList<String> tags = null;

		if (dataSetMetaDTO.getKeywords() != null
				&& !dataSetMetaDTO.getKeywords().isEmpty()) {

			tags = new ArrayList<String>(Arrays.asList(dataSetMetaDTO
					.getKeywords().split(",")));

		}

		DatasetBuilder datasetBuilder = new DatasetBuilder()
				.setName(dataSetMetaDTO.getDataSetName().replace("_", " "))
				.setTags(tags).setDescription(dataSetMetaDTO.getDataSetDesc())
				.setCategory(dataSetMetaDTO.getCategory())
				.setAttribution(dataSetMetaDTO.getAgencyName())
				.setAttributionLink(dataSetMetaDTO.getAttributionLink())
				.setPrivateMetadata(privateMetadata).setMetadata(metadata)
				.setColumns(generateColumns(dataSetMetaDTO));

		return datasetBuilder;
	}
	
	

	private ArrayList<Column> generateColumns(DataSetMetaDTO dataSetMetaDTO)  {
		ArrayList<Column> columns = new ArrayList<Column>();

		for (int i = 0; i < dataSetMetaDTO.getColumns().size(); i++) {

			if (dataSetMetaDTO.getColumns().get(i) != null
					&& !dataSetMetaDTO.getColumns().get(i).getColumnName()
							.isEmpty()) {
			
				logger.info("column Desc"
						+ dataSetMetaDTO.getColumns().get(i).getColumnDesc());
				logger.info("column Desc"
						+ dataSetMetaDTO.getColumns().get(i).getColumnName());
			
			
				columns.add(new com.socrata.model.importer.Column(null,
						dataSetMetaDTO.getColumns().get(i).getColumnName(),
						dataSetMetaDTO.getColumns().get(i).getColumnName()
								.replaceAll("[^A-Za-z0-9]", "_").toLowerCase(),
						dataSetMetaDTO.getColumns().get(i).getColumnDesc(),
						"text",
						i + 1, 200));
			}
		}

		return columns;
	}
}
