package gov.nyc.opendata.integration.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

/**
 * SplitFiles class is used to split a large file into multiple files
 * 
 */
public class SplitFiles {
	private final static String NEWLINE = System.getProperty("line.separator");
	private static final Logger logger = Logger.getLogger(SplitFiles.class);

	/**
	 * reads a the content of a file and splits them into multiple files.
	 * 
	 * @param filePath
	 *            the file to read
	 * 
	 * @param lines
	 *            the threshold limit
	 * 
	 * 
	 * @return the list of files
	 * 
	 * @throws IOException
	 */
	public static ArrayList<File> getDataFiles(String filePath, int lines)
			throws IOException {

		File file = new File(filePath);
		String filename = FilenameUtils.removeExtension(file.getName());
		String extension = FilenameUtils.getExtension(file.getName());
		String parentFolder = file.getParent();
		BufferedReader bufferedReader = new BufferedReader(new FileReader(
				filePath));

		try {

			StringBuffer stringBuffer = new StringBuffer();
			logger.info("After reading the file " + filePath);
			String line;
			ArrayList<File> fileList = new ArrayList<File>();
			int i = 0;
			int counter = 1;

			// performs the splitting
			while ((line = bufferedReader.readLine()) != null) {
				// line = line.replace("\"", "''");
				stringBuffer.append(line);
				stringBuffer.append(NEWLINE);
				i++;
				if (i >= lines) {
					// saves the lines in the file
					File f = saveFile(stringBuffer, filename + counter,
							extension, parentFolder);

					logger.info("Split files filename " + f.getName());
					logger.info("Split files filepath " + f.getAbsolutePath());
					fileList.add(f);

					// creates a new buffer, so the old can get garbage
					// collected.
					stringBuffer = new StringBuffer();
					i = 0;
					counter++;

				}
			}

			// add the last few lines to a new file
			if (counter > 1) {
				File f = saveFile(stringBuffer, filename + counter, extension,
						parentFolder);

				logger.info("Split files filename " + f.getName());
				logger.info("Split files filepath " + f.getAbsolutePath());

				fileList.add(f);

			} else {

				fileList.add(file);
			}

			bufferedReader.close();

			return fileList;
		} catch (IOException e) {
			logger.error("Exception while splitting the files", e);
			throw e;
		} finally {

			try {
				bufferedReader.close();
			} catch (IOException e) {
				logger.error("failed to close BufferedReader ", e);
			}
		}

	}

	/**
	 * 
	 * saves the stringBuffer into a file
	 * 
	 * @param stringBuffer
	 *            the string buffer
	 * @param filename
	 *            the file name
	 * 
	 * @param extension
	 *            the extension of the file that needs to be saved
	 * 
	 * @param parentFolder
	 *            the location where the file needs to be savesd
	 * 
	 * @return the saved file
	 * 
	 */
	private static File saveFile(StringBuffer stringBuffer, String filename,
			String extension, String parentFolder) throws IOException {

		System.out.println("Path is " + parentFolder);
		File createdFile = new File(parentFolder + "/" + filename + "."
				+ extension);
		FileWriter output = null;
		try {
			output = new FileWriter(createdFile);
			output.write(stringBuffer.toString());

		} catch (IOException e) {
			logger.error("Exception while saving the files", e);
			throw e;
		} finally {

			try {

				output.close();
			} catch (IOException e) {
				logger.error("failed to close FileWriter ", e);
			}
		}
		return createdFile;

	}
}
