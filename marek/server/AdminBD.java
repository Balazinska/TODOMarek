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
public class AdminBD {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String idAdmin;
	@Persistent
	private int idTodo;

	public AdminBD(int idTodo) {
		this.idAdmin = "0";
		this.idTodo = idTodo;
	}

	public String getIdAdmin() {
		return idAdmin;
	}

	public void setIdAdmin() {
		this.idAdmin = "0";
	}
	
	public int getIdTodo() {
		return idTodo;
	}

	public void setIdTodo(int idTodo) {
		this.idTodo = idTodo;
	}
}
