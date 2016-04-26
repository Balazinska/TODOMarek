/*
 *  @author Anna Balazinska  (abalazinska@hotmail.com)
*/
package marek.client.model;

import java.io.Serializable;

public class Todo implements Serializable {
	private static final long serialVersionUID = 1L;

	public String id = "";
	public int groupNr = -1;
	public String title = "";
	public int todoNr = -1;
	public String znaczek = "";
	public String style = "";
	public String description = "";
	public String updateFlag = "";

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

	public String getStyle() {
		return style;
	}
	
	public void setStyle(String style) {
		this.style = style;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getUpdateFlag() {
		return updateFlag;
	}
	public void setUpdateFlag(String updateFlag) {
		this.updateFlag = updateFlag;
	}
}
