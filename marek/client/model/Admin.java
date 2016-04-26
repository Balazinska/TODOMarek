/*
 *  @author Anna Balazinska  (abalazinska@hotmail.com)
*/
package marek.client.model;

import java.io.Serializable;

public class Admin implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public int idTodo;
	
	public int getIdTodo() {
		return idTodo;
	}
	
	public void setIdTodo(int idTodo) {
		this.idTodo = idTodo;
	}
}
