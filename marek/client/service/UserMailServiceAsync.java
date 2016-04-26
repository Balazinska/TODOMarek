/*
 *  @author Anna Balazinska  (abalazinska@hotmail.com)
*/
package marek.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserMailServiceAsync {
	void sendMail(String sendToP, String todoStrP, String grStStrP, String proposalStr, AsyncCallback<String> callback);
}
