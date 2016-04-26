/*
 *  @author Anna Balazinska  (abalazinska@hotmail.com)
*/
package marek.client.model;


import java.io.Serializable;

public class LoginInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	public String nickname = "";
	public String emailAddress = "";
	public String password = "";
	public String code = "";
	public String remember = "";
	public String updateFlag = "";

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRemember() {
		return remember;
	}

	public void setRemember(String remember) {
		this.remember = remember;
	}
	
	public String getUpdateFlag() {
		return updateFlag;
	}
	public void setUpdateFlag(String updateFlag) {
		this.updateFlag = updateFlag;
	}
}
