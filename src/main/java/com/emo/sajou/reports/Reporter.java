package com.emo.sajou.reports;

import java.io.File;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

public class Reporter {

	private final String email;
	private final String from;
	private final String name;
	private final File report;
	
	public Reporter(final String from, final String email, final String name, final File report) {
		this.email = email;
		this.name = name;
		this.report = report;
		this.from = from;
	}
	
	public void send()  {
		  EmailAttachment attachment = new EmailAttachment();
		  attachment.setPath(report.getAbsolutePath());
		  attachment.setDisposition(EmailAttachment.ATTACHMENT);
		  attachment.setDescription("Report for " + name);
		  attachment.setName(name + ".csv");

		  MultiPartEmail email = new MultiPartEmail();
		  email.setHostName("smtp.orange.fr");
		  
		  try {
		  email.addTo(this.email);
		  email.setFrom(from);
		  email.setSubject("Report for " + name);
		  email.setMsg("Report for " + name);

		  // add the attachment
		  email.attach(attachment);

		  // send the email
		  email.send();
		  }
		  catch(EmailException e) {
			  throw new RuntimeException("failed to send email for report " + name, e);
		  }
	}
}
