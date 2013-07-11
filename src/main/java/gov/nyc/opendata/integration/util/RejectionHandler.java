package gov.nyc.opendata.integration.util;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;
/**
 * RejectionHandler class - A handler for tasks that cannot be executed
 */
public class RejectionHandler implements RejectedExecutionHandler {
	private static final Logger logger = Logger.getLogger(RejectionHandler.class);
	  public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
	    logger.info(runnable.toString() + " : Rejected");
	  }
	}