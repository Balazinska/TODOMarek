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
public class TodoBD {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String id;
	@Persistent
	private int groupNr;
	@Persistent
	private String title;
	@Persistent
	private int todoNr;
	@Persistent
	private String znaczek;
	@Persistent
	private String description;
	
	public TodoBD(String id, int groupNr, String title, int todoNr, String znaczek, String description) {
		this.id = id;
		this.groupNr = groupNr;
		this.title = title;
		this.todoNr = todoNr;
		this.znaczek = znaczek;
		this.description = description;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public int getGroupNr() {
		return groupNr;
	}
	public void setGroupNr(int groupNr) {
		this.groupNr = groupNr;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public int getTodoNr() {
		return todoNr;
	}

	public void setTodoNr(int todoNr) {
		this.todoNr = todoNr;
	}

	public String getZnaczek() {
		return znaczek;
	}

	public void setZnaczek(String znaczek) {
		this.znaczek = znaczek;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
