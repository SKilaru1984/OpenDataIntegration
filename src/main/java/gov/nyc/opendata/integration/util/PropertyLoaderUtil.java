package gov.nyc.opendata.integration.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class PropertyLoaderUtil {

	static {
		loadLog4jProperties();
	}
	private static final Logger logger = Logger
			.getLogger(PropertyLoaderUtil.class);
	private static final ResourceBundle applicationProperties = loadApplicationProperties();

	public static final StringEncrypter STRING_ENCRYPTER = new StringEncrypter(
			applicationProperties.getString("encrypter.key"));

	public static final int PUBLISH_THRESHOLD = Integer
			.parseInt(applicationProperties.getString("publish.threshold"));

	// Socrata Config
	public static final String DOMAIN = applicationProperties
			.getString("socrata.domain");
	public static final String USER = applicationProperties
			.getString("socrata.user");
	public static final String PASSWORD = applicationProperties
			.getString("socrata.password");
	public static final String APPTOKEN = applicationProperties
			.getString("socrata.apptoken");

	// Proxy Config
	public static final String PROXY_HOST = applicationProperties
			.getString("proxy.host");
	public static final String PROXY_PORT = applicationProperties
			.getString("proxy.port");
	public static final boolean PROXY_ENABLED = Boolean
			.parseBoolean(applicationProperties.getString("proxy.enabled"));
	// public static final String PROXY_USER = applicationProperties
	// .getString("proxy.user");
	// public static final String PROXY_PASSWORD = applicationProperties
	// .getString("proxy.password");

	// MailSender Config
	public static final String MAIL_HOST = applicationProperties
			.getString("mail.smtp.server");
	public static final String MAIL_FROM = applicationProperties
			.getString("mail.from");

	public static final String MAIL_FAILURE_SUBJECT = applicationProperties
			.getString("mail.failure.subject");
	public static final String MAIL_FAILURE_BODY = applicationProperties
			.getString("mail.failure.body");

	public static final String MAIL_DB_FAILURE_SUBJECT = applicationProperties
			.getString("mail.db.failure.subject");
	public static final String MAIL_DB_FAILURE_BODY = applicationProperties
			.getString("mail.db.failure.body");

	public static final String MAIL_PUBLISH_FAILURE_SUBJECT = applicationProperties
			.getString("mail.publish.failure.subject");
	public static final String MAIL_PUBLISH_FAILURE_BODY = applicationProperties
			.getString("mail.publish.failure.body");

	public static final String MAIL_DB_UPDATE_FAIL_AFTER_PUBLISH_FAIL_SUBJECT = applicationProperties
			.getString("mail.db.update.fail.after.publish.fail.subject");
	public static final String MAIL_DB_UPDATE_FAIL_AFTER_PUBLISH_FAIL_BODY = applicationProperties
			.getString("mail.db.update.fail.after.publish.fail.body");

	public static final String MAIL_DB_UPDATE_FAIL_AFTER_PUBLISH_SUCCESS_SUBJECT = applicationProperties
			.getString("mail.db.update.fail.after.publish.success.subject");
	public static final String MAIL_DB_UPDATE_FAIL_AFTER_PUBLISH_SUCCESS_BODY = applicationProperties
			.getString("mail.db.update.fail.after.publish.success.body");

	public static final String MAIL_SUPPORT = applicationProperties
			.getString("mail.support");
	public static final String MAIL_PUBLISH_SUCCESS_RECIPIENT = applicationProperties
			.getString("mail.publish.success.recipient");
	public static final String MAIL_PUBLSIH_SUCCESS_SUBJECT = applicationProperties
			.getString("mail.publish.success.subject");
	public static final String MAIL_PUBLISH_SUCCESS_BODY = applicationProperties
			.getString("mail.publish.success.body");

	public static final String MAIL_DELETE_FILE_FAILURE_SUBJECT = applicationProperties
			.getString("mail.delete.file.failure.subject");
	public static final String MAIL_DELETE_FILE_FAILURE_BODY = applicationProperties
			.getString("mail.delete.file.failure.body");

//	public static final String MAIL_NO_ACTION_SUBJECT = applicationProperties
//			.getString("mail.no.action.subject");
//	public static final String MAIL_NO_ACTION_BODY = applicationProperties
//			.getString("mail.no.action.body");

	// File System Config

	// public static final String DATA_FILE_DELIMITER = applicationProperties
	// .getString("data.file.index.delimiter");
	// public static final String DATA_FILE_QUOTE_CHAR = applicationProperties
	// .getString("data.file.index.quote.char");

	// DataSource Config
	public static final String DATASOURCE_DRIVER_NAME = applicationProperties
			.getString("datasource.driver");
	public static final String DATASOURCE_URL = applicationProperties
			.getString("datasource.url");
	public static final String DATASOURCE_USERNAME = applicationProperties
			.getString("datasource.user");
	private static final String DATASOURCE_PASSWORD = applicationProperties
			.getString("datasource.password");
//	public static final String DATASOURCE_NAME = applicationProperties
//			.getString("datasource.name");

	// Concurrent Thread Count
	public static final int CORE_THREAD_COUNT = Integer
			.parseInt(applicationProperties.getString("core.thread.count"));
	public static final int MAX_THREAD_COUNT = Integer
			.parseInt(applicationProperties.getString("max.thread.count"));

	// Polling Hours
	public static final int POLL_HOURS = Integer.parseInt(applicationProperties
			.getString("poll.hours"));
	public static final int RETRY_TIME = Integer.parseInt(applicationProperties
			.getString("retry.time"));

	// Publish Flags
	public static final String UPSERT_FLAG = applicationProperties
			.getString("socrata.upsert.flag");
	public static final String APPEND_FLAG = applicationProperties
			.getString("socrata.append.flag");
	public static final String REPLACE_FLAG = applicationProperties
			.getString("socrata.replace.flag");

	// Status Flags
	public static final String JAVA_START = applicationProperties
			.getString("java.start.flag");
	public static final String JAVA_PUBLISHED = applicationProperties
			.getString("java.published.flag");
	public static final String JAVA_FAIL = applicationProperties
			.getString("java.fail.flag");

	public static final String DB_DECRYTED_PASSWORD = PropertyLoaderUtil.STRING_ENCRYPTER
			.decrypt(PropertyLoaderUtil.DATASOURCE_PASSWORD);

	// Excel Config

	public static final int DATA_DICTIONARY_METADATA_SHEET = Integer
			.parseInt(applicationProperties
					.getString("dataset.list.metadata.sheet"));

	public static final int DATA_DICTIONARY_METADATA_HEADERS_ROW = Integer
			.parseInt(applicationProperties
					.getString("dataset.list.metadata.headers.row"));

	public static final int DATA_DICTIONARY_METADATA_VALUES_ROW = Integer
			.parseInt(applicationProperties
					.getString("dataset.list.metadata.values.row"));

	public static final String DATASET_TITLE_HEADER = applicationProperties
			.getString("dataset.title.header");

	public static final String DATASET_DESCRIPTION_HEADER = applicationProperties
			.getString("dataset.description.header");

	public static final String DATASET_KEYWORDS_HEADER = applicationProperties
			.getString("dataset.keywords.header");

	public static final String DATASET_UPDATE_FREQUENCY_HEADER = applicationProperties
			.getString("dataset.update.frequency.header");

	public static final String DATASET_CATEGORY_HEADER = applicationProperties
			.getString("dataset.category.header");

	public static final String DATASET_ATTRIBUTION_LINK_HEADER = applicationProperties
			.getString("dataset.attribution.link.header");

	public static final String DATASET_ATTRIBUTION_HEADER = applicationProperties
			.getString("dataset.attribution.header");

	public static final String DATASET_DATA_DICTIONARY_HEADER = applicationProperties
			.getString("dataset.data.dictionary.header");

	public static final int DATA_DICTIONARY_COLUMNS_SHEET = Integer
			.parseInt(applicationProperties
					.getString("data.dictionary.columns.sheet"));

	public static final int DATA_DICTIONARY_COLUMNS_HEADERS_ROW = Integer
			.parseInt(applicationProperties
					.getString("data.dicitinary.columns.headers.row"));

	public static final int DATA_DICTIONARY_COLUMNS_VALUES_FIRST_ROW = Integer
			.parseInt(applicationProperties
					.getString("data.dicitinary.columns.values.first.row"));

	public static final String COLUMN_NAME_HEADER = applicationProperties
			.getString("column.name.header");

	public static final String COLUMN_DESC_HEADER = applicationProperties
			.getString("column.desc.header");
	public static final String COLUMN_TYPE_HEADER = applicationProperties
			.getString("column.type.header");

	public static void init() {
		logger.info("intializing properties and logger");
	}

	private static void loadLog4jProperties() {

		Properties log4jProps = null;
		InputStream inputStream = null;
		try {
			inputStream = OpenDataSocrataIntegrationConstants.class
					.getClassLoader().getResourceAsStream("log4j.properties");
			log4jProps = new Properties();
			log4jProps.load(inputStream);
			PropertyConfigurator.configure(log4jProps);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static ResourceBundle loadApplicationProperties() {

		ResourceBundle bundle = null;
		InputStream inputStream = null;
		try {
			inputStream = OpenDataSocrataIntegrationConstants.class
					.getClassLoader().getResourceAsStream(
							"NYCOpenDataIntegration.properties");
			bundle = new PropertyResourceBundle(inputStream);
			logger.info("Loaded external properties: ");
		} catch (Exception e) {
			logger.error("Error loading external properties: ", e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				logger.error("Error loading external properties: ", e);
			}
		}
		return bundle;
	}

}
