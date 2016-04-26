/*
 *  @author Anna Balazinska  (abalazinska@hotmail.com)
*/
package marek.server;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import marek.client.service.UserMailService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class MailServiceImpl extends RemoteServiceServlet implements UserMailService  {
	String returnMessage;

	public String sendMail(String sendToP, String todoStrP, String grStStrP, String proposalStr) throws UnsupportedEncodingException {
		returnMessage = new String ("Sending backup email to "
								+ sendToP + " succeded");
		//
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("abalazinska@hotmail.com",
					"TODO backup"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					sendToP, "Balazinska"));
			msg.setSubject("TODO backup - No reply for this mail.");
			//System.out.println(todoStrP+grStStrP+proposalStr);
			msg.setText(todoStrP+grStStrP+proposalStr);
			Transport.send(msg);

		} catch (MessagingException e) {
			System.out.println("MAIL: messaging exception:" + e);
			returnMessage =  "Error: "+e;
		}
		return returnMessage;
	}
}
