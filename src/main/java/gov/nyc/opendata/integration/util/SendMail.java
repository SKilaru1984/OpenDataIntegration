package gov.nyc.opendata.integration.util;

import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.stringtemplate.v4.*;

/**
 * SendMail class - All the success and failure notifications are sent to the
 * respective users
 */
public class SendMail {
	private static final Logger logger = Logger.getLogger(SendMail.class);

	/**
	 * 
	 * Send Mail to respective users of the dataset
	 * 
	 * @param emailRecepients
	 *            comma separated string with the emailRecepients
	 * 
	 * @param emailSubject
	 *            subject of the email
	 * 
	 * @param dataSetName
	 *            name of the dataset
	 * 
	 * @param emailBody
	 *            body of the email
	 * 
	 * 
	 */
	public static void sendMail(String emailRecepients, String emailSubject,
			String dataSetName, String emailBody) {
		try {
			logger.info("Sending Email Notification");
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			String dataSetPath = "";
			mailSender.setHost(PropertyLoaderUtil.MAIL_HOST);

			MimeMessage message = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(PropertyLoaderUtil.MAIL_FROM);

			helper.setTo(org.springframework.util.StringUtils
					.commaDelimitedListToStringArray(emailRecepients));
			String[] dataSetNameArray = null;
			if (dataSetName != null && !dataSetName.equals("")
					&& !dataSetName.isEmpty()) {
				ST emailSubjectTemplate = new ST(emailSubject);
				ST emailBodyTemplate = new ST(emailBody);
				dataSetNameArray = dataSetName.split("&");
				if (dataSetNameArray.length > 1) {
					dataSetName = dataSetNameArray[0];
					dataSetPath = dataSetNameArray[1];
					emailSubjectTemplate.add("dataset_name", dataSetName);
					helper.setSubject(emailSubjectTemplate.render());

					// Replace the tags in the body
					emailBodyTemplate.add("dataset_name", dataSetName);
					emailBodyTemplate.add("dataset_file_name", dataSetPath);
					helper.setText(emailBodyTemplate.render());

				} else {
					emailSubjectTemplate.add("dataset_name", dataSetName);
					helper.setSubject(emailSubjectTemplate.render());

					helper.setTo(org.springframework.util.StringUtils
							.commaDelimitedListToStringArray(emailRecepients));

					emailBodyTemplate.add("dataset_name", dataSetName);
					helper.setText(emailBodyTemplate.render());
				}
			} else {

				helper.setSubject(emailSubject);
				helper.setText(emailBody);
			}

			mailSender.send(message);
		} catch (Exception e) {
			logger.error("Mail Exception " + e.getMessage());
		}
	}
}
