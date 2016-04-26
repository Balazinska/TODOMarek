/*
 *  @author Anna Balazinska  (abalazinska@hotmail.com)
*/
package marek.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class LoginInfoBD {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String nickname;
	@Persistent
	private String emailAddress;
	@Persistent
	private String password;
	@Persistent
	private String code;
	@Persistent
	private String remember;
	
	public LoginInfoBD(String nickname, String emailAddress, String password,
			String code, String remember) {
		this.nickname = nickname;
		this.emailAddress = emailAddress;
		this.password = password;
		this.code = code;
		this.remember = remember;
	}

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
}
