package gov.nyc.opendata.integration.main;

import gov.nyc.opendata.integration.dao.JdbcServiceRequestDAO;
import gov.nyc.opendata.integration.dto.DataSetDTO;
import gov.nyc.opendata.integration.util.PropertyLoaderUtil;
import gov.nyc.opendata.integration.util.SendMail;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.socrata.api.HttpLowLevel;

import gov.nyc.opendata.integration.util.RejectionHandler;

/**
 * The CallSocrata class checks for all the datasets that have been generated
 * and processes these datasets across multiple threads
 */
public class CallSocrata {
	static {
		PropertyLoaderUtil.init();
	}

	private static final Logger logger = Logger.getLogger(CallSocrata.class);
//	private static ThreadPoolExecutor executor = null;

	// flag to check if all the threads have been processed
	private static volatile boolean finished = false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ThreadPoolExecutor executor = null;
		ArrayList<DataSetDTO> dsDTO = null;

		// Duration until which the database has to be polled for
		// any generated datasets
		Calendar pollDuration = Calendar.getInstance();
		pollDuration.add(Calendar.HOUR, PropertyLoaderUtil.POLL_HOURS);

		logger.info(" Polling duration " + pollDuration.getTime());

		while (!finished) {
			// Get the list of all the generated datasets
			dsDTO = JdbcServiceRequestDAO.getDataSetList();

			if (dsDTO != null && dsDTO.size() > 0) {

				// Create a blocking queue to hold all the submitted tasks
				// the size of the queue is set based on the number of datasets
				// to be processed
				BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(
						dsDTO.size());

				// Run multiple threads, the core and max thread count is
				// same in this case to avoid any threads getting dropped
				executor = new ThreadPoolExecutor(
						PropertyLoaderUtil.CORE_THREAD_COUNT,
						PropertyLoaderUtil.MAX_THREAD_COUNT, 15,
						TimeUnit.MINUTES, queue, new RejectionHandler());
				try {

					if (PropertyLoaderUtil.PROXY_ENABLED) {
						// Proxy Settings
						Properties sysProperties = System.getProperties();
						logger.info("Proxy is enabled");
						sysProperties.put("https.proxyHost",
								PropertyLoaderUtil.PROXY_HOST);
						sysProperties.put("https.proxyPort",
								PropertyLoaderUtil.PROXY_PORT);
						sysProperties.put("http.proxyHost",
								PropertyLoaderUtil.PROXY_HOST);
						sysProperties.put("http.proxyPort",
								PropertyLoaderUtil.PROXY_PORT);

						sysProperties.put("proxySet", "true");

					}

					// Creates a JDBC Importer with all the connection
					// information needed for connecting to the Socrata Portal

					final HttpLowLevel httpLowLevel = HttpLowLevel
							.instantiateBasic(
									PropertyLoaderUtil.DOMAIN,
									PropertyLoaderUtil.USER,
									PropertyLoaderUtil.STRING_ENCRYPTER
											.decrypt(PropertyLoaderUtil.PASSWORD),
									PropertyLoaderUtil.APPTOKEN);

					logger.info("dsDTO length " + dsDTO.size());

					// Execute each dataset
					for (DataSetDTO dataSetDTO : dsDTO) {

						executor.execute(new Socrata(dataSetDTO, httpLowLevel));

					}

					executor.shutdown();

					// Wait till all the datasets are processed
					while (!executor.isTerminated()) {
						Thread.sleep(2 * 60 * 1000);
					}

					logger.info("Terminated!");

					Thread.sleep(2 * 60 * 1000);

				} catch (Exception e) {

					logger.error(e);
					SendMail.sendMail(PropertyLoaderUtil.MAIL_SUPPORT,
							PropertyLoaderUtil.MAIL_FAILURE_SUBJECT, null,
							PropertyLoaderUtil.MAIL_FAILURE_BODY);
				}
			} else {
				// Check if the current time is before the poll Duration
				if ((Calendar.getInstance().getTime().after(pollDuration
						.getTime()))) {
					finished = true;
				} else {
					try {
						// Wait for 15 minutes till the next polling
						logger.info("No Generated Datasets found for processing ");
						Thread.sleep(PropertyLoaderUtil.RETRY_TIME);

					} catch (InterruptedException e) {

						logger.error("Exception while polling ", e);
						// Send failure notification to support team
						SendMail.sendMail(PropertyLoaderUtil.MAIL_SUPPORT,
								PropertyLoaderUtil.MAIL_FAILURE_SUBJECT, null,
								PropertyLoaderUtil.MAIL_FAILURE_BODY);

					}
				}

			}

		}
	}
}
