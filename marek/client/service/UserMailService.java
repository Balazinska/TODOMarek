/*
 *  @author Anna Balazinska  (abalazinska@hotmail.com)
*/
package marek.client.service;

import java.io.UnsupportedEncodingException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("userMailService")
public interface UserMailService extends RemoteService {

	String sendMail(String sendToP, String todoStrP, String grStStrP, String proposalStr) throws UnsupportedEncodingException;

}
