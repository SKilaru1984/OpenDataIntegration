package gov.nyc.opendata.integration.dao;

import java.io.File;
import java.util.ArrayList;

import gov.nyc.opendata.integration.dto.ColumnDTO;
import gov.nyc.opendata.integration.dto.DataSetIndexDTO;
import gov.nyc.opendata.integration.dto.DataSetMetaDTO;
import gov.nyc.opendata.integration.util.PropertyLoaderUtil;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * ExcelServiceRequestDAO class is used mainly to read the dataset file and data
 * dictionary file to get the metadata required for creating a dataset.
 * 
 */
public class ExcelServiceRequestDAO {

	private static final Logger logger = Logger
			.getLogger(ExcelServiceRequestDAO.class);

	/**
	 * reads the data dictionary file to get the column name, description and
	 * type index .
	 * 
	 * @param Sheet
	 *            the excel sheet to read
	 * 
	 * @param dataSetIndexDTO
	 *            to hold the index of the required column fields
	 * 
	 * @return dataSetIndexDTO
	 * 
	 * 
	 */
	private DataSetIndexDTO getDataSetColumnIndex(Sheet s,
			DataSetIndexDTO dataSetIndexDTO) {

		Row r1 = s
				.getRow(PropertyLoaderUtil.DATA_DICTIONARY_COLUMNS_HEADERS_ROW);

		for (int i = 0; i <= r1.getLastCellNum(); i++) {

			if (r1.getCell(i) != null && !r1.getCell(i).toString().isEmpty()) {

				if (r1.getCell(i)
						.toString()
						.equalsIgnoreCase(PropertyLoaderUtil.COLUMN_NAME_HEADER)) {

					dataSetIndexDTO.setColumnNameIndex(i);
				}

				else if (r1
						.getCell(i)
						.toString()
						.equalsIgnoreCase(PropertyLoaderUtil.COLUMN_DESC_HEADER)) {

					dataSetIndexDTO.setColumnDescIndex(i);
				} else if (r1
						.getCell(i)
						.toString()
						.equalsIgnoreCase(PropertyLoaderUtil.COLUMN_TYPE_HEADER)) {

					dataSetIndexDTO.setColumnTypeIndex(i);
				}
			}
		}
		logger.info("columnNameIndex " + dataSetIndexDTO.getColumnNameIndex());
		logger.info("columnDescIndex " + dataSetIndexDTO.getColumnDescIndex());
		logger.info("columnTypeIndex " + dataSetIndexDTO.getColumnTypeIndex());
		return dataSetIndexDTO;
	}

	/**
	 * reads the data dictionary file to get the column name, description and
	 * type .
	 * 
	 * @param dataSetIndexDTO
	 *            to hold the index of the required column fields
	 * 
	 * @param dataDictionaryFile
	 *            the location of the data dictionary
	 * 
	 * @param listOfFiles
	 *            all the files in the folder where the dataset file is located
	 * 
	 * @return list of columns
	 * 
	 * 
	 */
	private ArrayList<ColumnDTO> getColumnsFromDictionary(
			DataSetIndexDTO dataSetIndexDTO, String dataDictionaryFile,
			File[] listOfFiles) throws Exception {
		logger.info("dataDictionaryFile " + dataDictionaryFile);
		ArrayList<ColumnDTO> columnDTOList = new ArrayList<ColumnDTO>();
		for (File file : listOfFiles) {

			logger.info("Directory file "
					+ FilenameUtils.removeExtension(file.getName()));
			if (dataDictionaryFile.trim().equalsIgnoreCase(
					FilenameUtils.removeExtension(file.getName()).trim())) {

				Workbook workBook = WorkbookFactory.create(new File(file
						.getPath()));

				Sheet s = workBook
						.getSheetAt(PropertyLoaderUtil.DATA_DICTIONARY_COLUMNS_SHEET);
				dataSetIndexDTO = getDataSetColumnIndex(s, dataSetIndexDTO);

				for (int i = PropertyLoaderUtil.DATA_DICTIONARY_COLUMNS_VALUES_FIRST_ROW; i <= s
						.getLastRowNum(); i++) {
					Row r = s.getRow(i);
					ColumnDTO columnDTO = new ColumnDTO();
					if (r != null
							&& r.getCell(dataSetIndexDTO.getColumnNameIndex()) != null
							&& !r.getCell(dataSetIndexDTO.getColumnNameIndex())
									.toString().isEmpty()) {
						columnDTO.setColumnName(r.getCell(
								dataSetIndexDTO.getColumnNameIndex())
								.toString());

						logger.info("Column Name is "
								+ columnDTO.getColumnName());

						columnDTO.setColumnDesc((r.getCell(dataSetIndexDTO
								.getColumnDescIndex()) != null && !r
								.getCell(dataSetIndexDTO.getColumnDescIndex())
								.toString().isEmpty()) ? r.getCell(
								dataSetIndexDTO.getColumnDescIndex())
								.toString() : "");

						columnDTO.setColumnType((r.getCell(dataSetIndexDTO
								.getColumnTypeIndex()) != null && !r
								.getCell(dataSetIndexDTO.getColumnTypeIndex())
								.toString().isEmpty()) ? r.getCell(
								dataSetIndexDTO.getColumnTypeIndex())
								.toString() : "text");

						columnDTOList.add(columnDTO);
					}

				}
				break;
			}
		}

		return columnDTOList;

	}

	/**
	 * reads the dataset file to get the required metadata fields index .
	 * 
	 * @param Workbook
	 *            the excel file to read
	 * 
	 * 
	 * @return dataSetIndexDTO
	 * 
	 * 
	 */
	private DataSetIndexDTO getDataSetMetaIndex(Sheet s) {
		DataSetIndexDTO dataSetIndexDTO = new DataSetIndexDTO();

		// Sheet s = workBook
		// .getSheetAt(PropertyLoaderUtil.DATA_DICTIONARY_METADATA_SHEET);

		logger.info("Sheet for meta data Created ");

		Row r1 = s
				.getRow(PropertyLoaderUtil.DATA_DICTIONARY_METADATA_HEADERS_ROW);

		logger.info("r1 " + r1.getLastCellNum());

		logger.info("r1 " + r1.getRowNum());

		for (int i = 0; i <= r1.getLastCellNum(); i++) {

			if (r1.getCell(i) != null && !r1.getCell(i).toString().isEmpty()) {

				if (r1.getCell(i)
						.toString()
						.equalsIgnoreCase(
								PropertyLoaderUtil.DATASET_TITLE_HEADER)) {

					dataSetIndexDTO.setDataSetTitleIndex(i);
				}

				else if (r1
						.getCell(i)
						.toString()
						.equalsIgnoreCase(
								PropertyLoaderUtil.DATASET_DESCRIPTION_HEADER)) {

					dataSetIndexDTO.setDataSetDescIndex(i);
				}

				else if (r1
						.getCell(i)
						.toString()
						.equalsIgnoreCase(
								PropertyLoaderUtil.DATASET_KEYWORDS_HEADER)) {

					dataSetIndexDTO.setDataSetKeyWordsIndex(i);
				}

				else if (r1
						.getCell(i)
						.toString()
						.equalsIgnoreCase(
								PropertyLoaderUtil.DATASET_UPDATE_FREQUENCY_HEADER)) {

					dataSetIndexDTO.setUpdateFreqIndex(i);
				}

				else if (r1
						.getCell(i)
						.toString()
						.equalsIgnoreCase(
								PropertyLoaderUtil.DATASET_CATEGORY_HEADER)) {

					dataSetIndexDTO.setCategoryIndex(i);
				}

				else if (r1
						.getCell(i)
						.toString()
						.equalsIgnoreCase(
								PropertyLoaderUtil.DATASET_ATTRIBUTION_LINK_HEADER)) {

					dataSetIndexDTO.setAttributionLinkIndex(i);
				}

				else if (r1
						.getCell(i)
						.toString()
						.equalsIgnoreCase(
								PropertyLoaderUtil.DATASET_ATTRIBUTION_HEADER)) {

					dataSetIndexDTO.setAttributionIndex(i);
				}

				else if (r1
						.getCell(i)
						.toString()
						.equalsIgnoreCase(
								PropertyLoaderUtil.DATASET_DATA_DICTIONARY_HEADER)) {

					dataSetIndexDTO.setDataDictionaryIndex(i);
				}
			}

		}

		logger.info("dataSetTitleIndex "
				+ dataSetIndexDTO.getDataSetTitleIndex());
		logger.info("dataSetDescIndex " + dataSetIndexDTO.getDataSetDescIndex());
		logger.info("dataSetKeyWordsIndex "
				+ dataSetIndexDTO.getDataSetKeyWordsIndex());
		logger.info("updateFreqIndex " + dataSetIndexDTO.getUpdateFreqIndex());
		logger.info("dataDictionaryIndex "
				+ dataSetIndexDTO.getDataDictionaryIndex());
		logger.info("categoryIndex " + dataSetIndexDTO.getCategoryIndex());
		logger.info("attributionIndex " + dataSetIndexDTO.getAttributionIndex());
		logger.info("attributionLinkIndex "
				+ dataSetIndexDTO.getAttributionLinkIndex());

		return dataSetIndexDTO;

	}

	/**
	 * reads the dataset file to get the metadata required to create the
	 * dataset.
	 * 
	 * @param dataSetFile
	 *            the location of the dataset file
	 * 
	 * @param dataSetIndexDTO
	 *            to hold the index of the required column fields
	 * 
	 * @return DataSetMetaDTO
	 * 
	 * 
	 */
	public DataSetMetaDTO getDataSetMetaData(String dataSetFile)
			throws Exception {

		logger.info("dataset File " + dataSetFile);
		File[] listOfFiles = new File(new File(dataSetFile).getParent())
				.listFiles();
		Workbook myWorkBook = WorkbookFactory.create(new File(dataSetFile));

		Sheet sheet = myWorkBook
				.getSheetAt(PropertyLoaderUtil.DATA_DICTIONARY_METADATA_SHEET);
		DataSetIndexDTO dataSetIndexDTO = getDataSetMetaIndex(sheet);

		DataSetMetaDTO dataSetMetaDTO = new DataSetMetaDTO();

		logger.info("Last Row of dataset list " + sheet.getLastRowNum());

		Row r = sheet
				.getRow(PropertyLoaderUtil.DATA_DICTIONARY_METADATA_VALUES_ROW);

		if (r != null
				&& r.getCell(dataSetIndexDTO.getDataDictionaryIndex()) != null
				&& !r.getCell(dataSetIndexDTO.getDataDictionaryIndex())
						.toString().isEmpty()) {

			dataSetMetaDTO.setDataSetDesc(r.getCell(
					dataSetIndexDTO.getDataSetDescIndex()).toString());
			dataSetMetaDTO.setDataSetName(r.getCell(
					dataSetIndexDTO.getDataSetTitleIndex()).toString());
			dataSetMetaDTO.setKeywords(r.getCell(
					dataSetIndexDTO.getDataSetKeyWordsIndex()).toString());
			dataSetMetaDTO.setUpdateFreq(r.getCell(
					dataSetIndexDTO.getUpdateFreqIndex()).toString());
			dataSetMetaDTO.setAgencyName(r.getCell(
					dataSetIndexDTO.getAttributionIndex()).toString());

			if (r.getCell(dataSetIndexDTO.getAttributionLinkIndex()) != null
					&& !r.getCell(dataSetIndexDTO.getAttributionLinkIndex())
							.toString().equals("")) {
				dataSetMetaDTO.setAttributionLink(r.getCell(
						dataSetIndexDTO.getAttributionLinkIndex()).toString());
			}

			dataSetMetaDTO.setCategory(r.getCell(
					dataSetIndexDTO.getCategoryIndex()).toString());

			dataSetMetaDTO.setColumns(getColumnsFromDictionary(dataSetIndexDTO,
					r.getCell(dataSetIndexDTO.getDataDictionaryIndex())
							.toString(), listOfFiles));

		}

		return dataSetMetaDTO;
	}

}
