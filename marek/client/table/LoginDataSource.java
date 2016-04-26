/*
 *  @author Anna Balazinska  (abalazinska@hotmail.com)
*/
package marek.client.table;

import java.util.ArrayList;
import java.util.List;

import marek.client.model.LoginInfo;

public class LoginDataSource {
	private final List<LoginInfo> loginL;
	private List<String> loginHeader;
	
	public LoginDataSource(List<LoginInfo> loginL) {
		loginHeader = new ArrayList<String>();
		loginHeader.add("Nickname");
		loginHeader.add("Email Address");
		loginHeader.add("Password");
		loginHeader.add("Code");
		loginHeader.add("Remember me");
		this.loginL = loginL;
	}
	
	public List<LoginInfo> getLoginInfo() {
		return loginL;
	}
	
	public List<String> getTableHeader() {
		return loginHeader;
	}

}

