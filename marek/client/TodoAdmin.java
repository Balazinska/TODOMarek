/*
 *  @author Anna Balazinska  (abalazinska@hotmail.com)
*/
package marek.client;

import java.util.ArrayList;
import java.util.List;

import marek.client.model.Todo;
import marek.client.service.MyDatastoreService;
import marek.client.service.MyDatastoreServiceAsync;
import marek.client.service.UserMailService;
import marek.client.service.UserMailServiceAsync;
import marek.client.DoubleClickTable;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;

public class TodoAdmin {
	// Buttons - function
	final Button newButton = new Button("Add");
	final Button deleteButton = new Button("Delete");
	final Button saveButton = new Button("Save");
	final Button cancelButton = new Button("Cancel");
	final HorizontalPanel funcPanel = new HorizontalPanel();
	final HorizontalPanel znaczekPanel = new HorizontalPanel();
	final VerticalPanel leftPanel = new VerticalPanel();
	// Znaczki
	static TextBox gw = new TextBox();
	static TextBox dolar = new TextBox();
	static TextBox flesh = new TextBox();
	static String znak = "";

	// Add category
	final Button catButton = new Button("Add category");
	final TextBox category = new TextBox();
	final VerticalPanel catMenu = new VerticalPanel();
	// Search
	public static TextBox search = new TextBox();
	final Button searchButton = new Button("Search");
	final VerticalPanel searchMenu = new VerticalPanel();
	// Mail
	String todoSTR;
	static TextBox emailAddress = new TextBox();
	final Button sendButton = new Button("Send data to");
	final VerticalPanel mailMenu = new VerticalPanel();
	//
	static Label titleH = new Label("Category");
	static Label znaczekH = new Label(" ");
	static Label todoNrH = new Label("Nr");
	static Label descriptionH = new Label("Description");
	//
	//
	static ArrayList<String> id = new ArrayList<String>();
	static ArrayList<Integer> groupNr = new ArrayList<Integer>();
	static ArrayList<TextBox> title = new ArrayList<TextBox>();
	static ArrayList<TextBox> todoNr = new ArrayList<TextBox>();
	static ArrayList<TextBox> znaczek = new ArrayList<TextBox>();
	static ArrayList<String> style = new ArrayList<String>();
	static ArrayList<TextBox> description = new ArrayList<TextBox>();
	static ArrayList<String> updateFlag = new ArrayList<String>();
	//
	//
	static int rowNr = -1;
	private static int rowSel = -1;
	private static String addTitle = "";
	private static String prevTitle = "";
	private static int addGroup = -1;
	private static int addNr = -1;
	private static int lineSum = 0;
	private static Boolean groupRec = false;

	private static HTMLTable.RowFormatter rf;
	private final MyDatastoreServiceAsync myDatastoreService = GWT
			.create(MyDatastoreService.class);

	private final UserMailServiceAsync userMailService = GWT
			.create(UserMailService.class);

	public HorizontalPanel setMenus() {
		HorizontalPanel todoFunc = new HorizontalPanel();
		//
		newButton.addStyleName("functionButton");
		newButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Drop : cancel drag&drop
				rf = Marektodo.todo.getRowFormatter();
				if (rowSel > -1) {
					rowNr = rowSel;
					rf.removeStyleName(rowSel, "wybrany");
					rowSel = -1;
				} else {
					if (rowNr > -1)
						rf.removeStyleName(rowNr, "tableRow");
				}
				if (rowNr <= -1) {
					Window.alert("Which category?");
					return;
				}

				int line = 0;
				prevTitle = title.get(rowNr).getText();
				addTitle = title.get(rowNr).getText();
				addGroup = groupNr.get(rowNr);

				if (rowNr == title.size() - 1) {
					line = title.size();
					addNr = Integer.parseInt(todoNr.get(rowNr).getText()) + 1;
					addOneRow(title.size(), -1, "A");
					rowNr = title.size() - 1;
				} else {
					addNr = Integer.parseInt(todoNr.get(rowNr).getText());
					line = rowNr + 1;
					for (int i = rowNr + 1; i <= title.size(); i++) {
						if (!addTitle.equals(title.get(i - 1).getText())) {
							line = i - 1;
							break;
						} else {
							if (addNr < Integer.parseInt(todoNr.get(i)
									.getText()))
								addNr = Integer.parseInt(todoNr.get(i)
										.getText());
						}
					}
					addNr = addNr + 1;
					// Window.alert("add="+addTitle+" line="+line+" gr="+addGroup+
					// " nr="+addNr);
					Marektodo.todo.insertRow(line);
					addOneRow(line, -1, "I");
					rowNr = line;
				}
				rf.setStyleName(rowNr, "tableRow");
			}
		});
		deleteButton.addStyleName("functionButton");
		deleteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Drop : cancel drag&drop
				rf = Marektodo.todo.getRowFormatter();
				if (rowSel > -1) {
					rowNr = rowSel;
					rf.removeStyleName(rowSel, "wybrany");
					rowSel = -1;
				} else {
					if (rowNr > -1)
						rf.removeStyleName(rowNr, "tableRow");
				}
				if (rowNr <= -1) {
					Window.alert("Nothing to delete.");
					return;
				}

				if (updateFlag.get(rowNr).equals("I")) {
					removeFromList(rowNr);
					Marektodo.todo.removeRow(rowNr);
					rowNr = -1;
				} else {
					Boolean yesNo = Window
							.confirm("Do you really want to delete this 'todo'? No undo.");
					if (yesNo) {
						// Window.alert("id="+id.get(rowNr));
						myDatastoreService.todoDelete(id.get(rowNr),
								new AsyncCallback<String>() {
									public void onFailure(Throwable error) {
										Window.alert(error.getMessage());
									}

									public void onSuccess(String result) {
										removeFromList(rowNr);
										Marektodo.todo.removeRow(rowNr);
										rowNr = -1;
									}
								});
					}
				}
			}
		});
		//
		saveButton.addStyleName("functionButton");
		saveButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Drop : cancel drag&drop
				rf = Marektodo.todo.getRowFormatter();
				if (rowSel > -1) {
					rowNr = rowSel;
					rf.removeStyleName(rowSel, "wybrany");
					rowSel = -1;
				} else {
					if (rowNr > -1)
						rf.removeStyleName(rowNr, "tableRow");
				}
				if (groupRec) {
					// Group number
					String prevCat = title.get(0).getText();
					int nrGr = 1;
					int nrL = 0;
					for (int i = 0; i < updateFlag.size(); i++) {
						if (prevCat.equals(title.get(i).getText())) {
							nrL++;
							if(Integer.parseInt(todoNr.get(i).getText()) != nrL) {
								todoNr.get(i).setText(Integer.toString(nrL));
								if (updateFlag.get(i).isEmpty())
									updateFlag.set(i, "U");
							}
							if(groupNr.get(i) != nrGr) {
								groupNr.set(i, nrGr);
								if (updateFlag.get(i).isEmpty())
									updateFlag.set(i, "U");
							}
						} else {
							prevCat = title.get(i).getText();
							nrGr++;
							nrL = 1;
							if (groupNr.get(i) != nrGr) {
								groupNr.set(i, nrGr);
								if (updateFlag.get(i).isEmpty())
									updateFlag.set(i, "U");
							}
							if (Integer.parseInt(todoNr.get(i).getText()) != nrL) {
								todoNr.get(i).setText(Integer.toString(nrL));
								if (updateFlag.get(i).isEmpty())
									updateFlag.set(i, "U");
							}
						}
					
				}
				}
				List<Todo> updList = new ArrayList<Todo>();
				for (int i = 0; i < updateFlag.size(); i++) {
					if (updateFlag.get(i).equals("I") 
							&& description.get(i).getText().trim().isEmpty())
						continue;
					Todo rec = new Todo();
					rec.setId(id.get(i));
					rec.setGroupNr(groupNr.get(i));
					rec.setTitle(title.get(i).getText());
					rec.setTodoNr(Integer.parseInt(todoNr.get(i).getText()));
					rec.setZnaczek(znaczek.get(i).getText());
					rec.setStyle(style.get(i));
					rec.setDescription(description.get(i).getText());
					rec.setUpdateFlag(updateFlag.get(i));
					//
					if (updateFlag.get(i).equals("I")) {
						int id = Marektodo.adminRec.getIdTodo() + 1;
						Marektodo.adminRec.setIdTodo(Marektodo.adminRec
								.getIdTodo() + 1);
						rec.setId(Integer.toString(id));
					}
					//
					// Window.alert("title="+rec.getTitle()
					// +" gr.="+rec.getGroupNr()
					// +" todo="+rec.getTodoNr()
					// +" zn="+rec.getZnaczek()
					// +" d="+rec.getDescription());
					updList.add(rec);
				}
				if (updList.size() == 0) {
					Window.alert("Nothing to save");
					return;
				}
				// Window.alert("upd size="+updList.size());
				myDatastoreService.updateAll(updList,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								Window.alert(caught.getMessage());
							}

							public void onSuccess(String result) {
								if (!result.equals("OK")) {
									Window.alert(result);
								} else {
									Window.alert("Save success");
									Marektodo todo = new Marektodo();
									todo.selectTodo(search.getText());
								}
							}
						});
			}
		});
		//
		cancelButton.addStyleName("functionButton");
		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Drop : cancel drag&drop
				rf = Marektodo.todo.getRowFormatter();
				if (rowSel > -1) {
					rowNr = rowSel;
					rf.removeStyleName(rowSel, "wybrany");
					rowSel = -1;
				} else {
					if (rowNr > -1)
						rf.removeStyleName(rowNr, "tableRow");
				}

				Marektodo todo = new Marektodo();
				todo.selectTodo(search.getText());
			}
		});
		funcPanel.setSpacing(5);
		funcPanel.add(newButton);
		funcPanel.add(deleteButton);
		funcPanel.add(saveButton);
		funcPanel.add(cancelButton);
		//
		gw.setText("*");
		gw.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				znak = "*";
				gw.addStyleName("wybrany");
			}
		});
		gw.setStyleName("znak");
		gw.addStyleName("red");
		gw.setReadOnly(true);
		dolar.setText("$");
		dolar.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Drop : cancel drag&drop
				rf = Marektodo.todo.getRowFormatter();
				if (rowSel > -1) {
					rowNr = rowSel;
					rf.removeStyleName(rowSel, "wybrany");
					rowSel = -1;
				}
				znak = "$";
				dolar.addStyleName("wybrany");
			}
		});
		dolar.setStyleName("znak");
		dolar.addStyleName("blue");
		dolar.setReadOnly(true);
		flesh.setText("->");
		flesh.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Drop : cancel drag&drop
				rf = Marektodo.todo.getRowFormatter();
				if (rowSel > -1) {
					rowNr = rowSel;
					rf.removeStyleName(rowSel, "wybrany");
					rowSel = -1;
				}
				znak = "->";
				flesh.addStyleName("wybrany");
			}
		});
		flesh.setStyleName("znak");
		flesh.addStyleName("green");
		flesh.setReadOnly(true);

		znaczekPanel.setSpacing(2);
		znaczekPanel.setStyleName("padTop");
		// znaczekPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		znaczekPanel.add(gw);
		znaczekPanel.add(dolar);
		znaczekPanel.add(flesh);

		leftPanel.add(funcPanel);
		leftPanel.add(znaczekPanel);

		//
		catButton.addStyleName("functionButton");
		catButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Drop : cancel drag&drop
				rf = Marektodo.todo.getRowFormatter();
				if (rowSel > -1) {
					rowNr = -1;
					rf.removeStyleName(rowSel, "wybrany");
					rowSel = -1;
				}
				if (category.getText().isEmpty()) {
					Window.alert("Category is empty");
					return;
				}
				//
				for(int i=0; i<title.size(); i++) {
					if(category.getText().equals(title.get(i).getText())) {
						Window.alert("Category already exists.");
						rowNr = i;
						rf.setStyleName(rowNr, "tableRow");
						return;
					}
				}
				if (title.size() == 0) {
					prevTitle = "";
					addTitle = category.getText();
					addGroup = 1;
					addNr = 1;
					addOneRow(0, -1, "A");
				} else {
					prevTitle = title.get(title.size() - 1).getText();
					addTitle = category.getText();
					addGroup = groupNr.get(title.size() - 1) + 1;
					addNr = 1;
					addOneRow(title.size(), -1, "A");
				}
				category.setText("");
			}
		});
		category.setSize("150px", "20px");
		catMenu.setSpacing(5);
		catMenu.setStyleName("funcMenu");
		catMenu.add(catButton);
		catMenu.add(category);

		//
		sendButton.addStyleName("functionButton");
		sendButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Drop : cancel drag&drop
				rf = Marektodo.todo.getRowFormatter();
				if (rowSel > -1) {
					rowNr = -1;
					rf.removeStyleName(rowSel, "wybrany");
					rowSel = -1;
				}
				myDatastoreService.todoInString(new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}

					public void onSuccess(String result) {
						todoSTR = result;
						sendData();
					}
				});
			}
		});
		emailAddress.setSize("300px", "20px");
		mailMenu.setSpacing(5);
		mailMenu.setStyleName("funcMenu");
		mailMenu.add(sendButton);
		mailMenu.add(emailAddress);
		//
		searchButton.setStyleName("functionButton");
		searchButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				for (int i = 0; i < updateFlag.size(); i++) {
					if (!updateFlag.get(i).isEmpty()) {
						Boolean yesNo = Window
								.confirm("You have not saved notes. Continue?");
						if (!yesNo) {
							search.setText("");
							return;
						}
					}
				}
				// Drop : cancel drag&drop
				rf = Marektodo.todo.getRowFormatter();
				if (rowSel > -1) {
					rowNr = -1;
					rf.removeStyleName(rowSel, "wybrany");
					rowSel = -1;
				}
				String filter = search.getText();
				search.setText("");
				Marektodo todo = new Marektodo();
				todo.selectTodo(filter);
			}
		});
		searchMenu.setSpacing(5);
		searchMenu.setStyleName("funcMenu");
		searchMenu.add(searchButton);
		searchMenu.add(search);
		//
		// Todos header
		titleH.setStyleName("codeBox100");
		znaczekH.setStyleName("codeBox20");
//		todoNrH.setStyleName("codeBox20");
		descriptionH.setStyleName("textArea");

		Marektodo.todoH.setCellPadding(2);
		Marektodo.todoH.setCellSpacing(1);
		Marektodo.todoH.addStyleName("addBorderTLRB");

		Marektodo.todoH.setWidget(0, 0, titleH);
		Marektodo.todoH.setWidget(0, 1, znaczekH);
//		Marektodo.todoH.setWidget(0, 2, todoNrH);
		Marektodo.todoH.setWidget(0, 2, descriptionH);
		Marektodo.todoH.getRowFormatter().addStyleName(0, "tableHeader");
		Marektodo.todoH.getCellFormatter().setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_CENTER);
		Marektodo.todoH.getCellFormatter().setHorizontalAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_CENTER);
		Marektodo.todoH.getCellFormatter().setHorizontalAlignment(0, 2,
				HasHorizontalAlignment.ALIGN_CENTER);
//		Marektodo.todoH.getCellFormatter().setHorizontalAlignment(0, 3,
//				HasHorizontalAlignment.ALIGN_CENTER);

		// todoFunc.setSpacing(5);
		todoFunc.add(leftPanel);
		todoFunc.add(catMenu);
		todoFunc.add(searchMenu);
		todoFunc.add(mailMenu);

		return todoFunc;
	}

	public void todoDisplay() {
		initArrays();
		//
		Marektodo.todo = new DoubleClickTable();

		Marektodo.todo.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
//				GWT.log("SIMPLEstart row="+rowNr+" sel="+rowSel);
				com.google.gwt.user.client.ui.HTMLTable.Cell cell = Marektodo.todo
						.getCellForEvent(event);
				if (rowNr > -1) {
					rf.removeStyleName(rowNr, "tableRow");
				}
				rf = Marektodo.todo.getRowFormatter();
				rowNr = cell.getRowIndex();
				// Drop : cancel drag&drop
				if (rowSel == rowNr) {
					rf.removeStyleName(rowSel, "wybrany");
					rowSel = -1;
					return;
				}
				if (rowSel > -1) {
					dropDragged();
					return;
				}
				// Window.alert("1 clicks row=" + rowNr + " sel=" + rowSel);
				rf.setStyleName(rowNr, "tableRow");
				if (updateFlag.get(rowNr).isEmpty()) {
					updateFlag.set(rowNr, "U");
				}
				if (cell.getCellIndex() == 1) {
					if (znaczek.get(rowNr).getText().isEmpty()) {
						if (cell.getCellIndex() == 1) {
							if (!znak.isEmpty()) {
								znaczek.get(rowNr).setText(znak);
								znaczek.get(rowNr).setStyleName("znak");
								if (znak.equals(gw.getText())) {
									znaczek.get(rowNr).addStyleName("red");
									gw.removeStyleName("wybrany");
								}
								if (znak.equals(dolar.getText())) {
									znaczek.get(rowNr).addStyleName("blue");
									dolar.removeStyleName("wybrany");
								}
								if (znak.equals(flesh.getText())) {
									znaczek.get(rowNr).addStyleName("green");
									flesh.removeStyleName("wybrany");
								}
								// Marektodo.todo.setWidget(rowNr, 1, marker);
								znak = "";
							}
						}
					} else {
						znaczek.get(rowNr).setText("");
						znaczek.get(rowNr).setStyleName("codeBox20");
					}
				}
				if (cell.getCellIndex() == 2) {
					todoNr.get(rowNr).selectAll();
				}
//				GWT.log("SIMPLE end row="+rowNr+" sel="+rowSel);
			}
		});
		// Drop line
		Marektodo.todo.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
//				GWT.log("DCstart: sel="+rowSel+" row="+rowNr);
				rf = Marektodo.todo.getRowFormatter();
				if (rowSel > -1) {
					rf.removeStyleName(rowSel, "wybrany");
				}
				if (rowNr > -1) {
					rf.removeStyleName(rowNr, "tableRow");
				}
				Cell cell = Marektodo.todo.getCellForEvent(event);
				rowSel = cell.getRowIndex();
				rowNr = rowSel;
				rf.addStyleName(rowSel, "wybrany");
				//
				lineSum = 0;
				for (int i = 0; i < groupNr.size(); i++) {
					if (groupNr.get(rowSel) ==groupNr.get(i))
						lineSum++;
				}
//				GWT.log("DC: sel="+rowSel+" row="+rowNr);
			}

		});
		// Display datastore TODO data
		Marektodo.todo.setCellPadding(1);
		Marektodo.todo.setCellSpacing(1);
		Marektodo.todo.setStyleName("table");
		if (Marektodo.allTodo.size() > 0) {
			prevTitle = "";
			for (int i = 0; i < Marektodo.allTodo.size(); i++) {
				addTitle = Marektodo.allTodo.get(i).getTitle();
				addOneRow(i, i, "A");
			}
		}
	}

	private void initArrays() {
		id = new ArrayList<String>();
		groupNr = new ArrayList<Integer>();
		title = new ArrayList<TextBox>();
		todoNr = new ArrayList<TextBox>();
		znaczek = new ArrayList<TextBox>();
		style = new ArrayList<String>();
		description = new ArrayList<TextBox>();
		updateFlag = new ArrayList<String>();
	}

	private void removeFromList(int row) {
		if (row + 1 < id.size()) {
			if (Marektodo.todo.getWidget(row, 0) != null) {
				if (Marektodo.todo.getWidget(row + 1, 0) == null) {
					Marektodo.todo.setWidget(row + 1, 0, title.get(row + 1));
				}
			}
		}
		id.remove(row);
		groupNr.remove(row);
		title.remove(row);
		todoNr.remove(row);
		znaczek.remove(row);
		style.remove(row);
		description.remove(row);
		updateFlag.remove(row);
	}

	//
	private void addOneRow(int row, int index, String insertAdd) {
		// Window.alert("row=" + row + " in=" + index + " fl=" + insertAdd);
		// Not displayed
		if (index <= -1) {
			if (insertAdd.equals("I")) {
				id.add(row, "-1");
				groupNr.add(row, addGroup);
				style.add(row, "");
				updateFlag.add(row, "I");
			} else {
				id.add("-1");
				groupNr.add(addGroup);
				style.add("");
				updateFlag.add("I");
			}
		} else {
			String idT = Marektodo.allTodo.get(index).getId();
			int gr = Marektodo.allTodo.get(index).getGroupNr();
			String st = Marektodo.allTodo.get(index).getStyle();
			String fl = Marektodo.allTodo.get(index).getUpdateFlag();
			if (insertAdd.equals("I")) {
				id.add(row, idT);
				groupNr.add(row, gr);
				style.add(row, st);
				updateFlag.add(row, fl);
			} else {
				id.add(idT);
				groupNr.add(gr);
				style.add(st);
				updateFlag.add(fl);
			}
		}
		// Title
		TextBox tb1 = new TextBox();
		int stNr = groupNr.get(row);
		if (stNr > 10)
			stNr = stNr - 10;
		tb1.setStyleName("cat" + stNr);
		tb1.setReadOnly(true);
		tb1.setText(addTitle);
		if (insertAdd.equals("I"))
			title.add(row, tb1);
		else
			title.add(tb1);
		if (!prevTitle.equals(addTitle)) {
			Marektodo.todo.setWidget(row, 0, tb1);
			prevTitle = addTitle;
		}
		//
		// znaczek
		TextBox tb2 = new TextBox();
		if (index <= -1) {
			tb2.setText("");
			tb2.setStyleName("codeBox20");
		} else {		
			if (!Marektodo.allTodo.get(index).getZnaczek().isEmpty()) {
				tb2.setText(Marektodo.allTodo.get(index).getZnaczek());
				tb2.setStyleName("znak");
				if (Marektodo.allTodo.get(index).getZnaczek()
						.equals(gw.getText())) {
					tb2.addStyleName("red");
				}
				if (Marektodo.allTodo.get(index).getZnaczek()
						.equals(dolar.getText())) {
					tb2.addStyleName("blue");
				}
				if (Marektodo.allTodo.get(index).getZnaczek()
						.equals(flesh.getText())) {
					tb2.addStyleName("green");
				}
			} else {
				tb2.setStyleName("codeBox20");
			}
		}
		if (insertAdd.equals("I"))
			znaczek.add(row, tb2);
		else
			znaczek.add(tb2);
		Marektodo.todo.setWidget(row, 1, tb2);
		//
		// TodoNR
		TextBox tb3 = new TextBox();
		tb3.setStyleName("codeBox20");
		if (index <= -1) {
			tb3.setText(Integer.toString(addNr));
		} else {
			tb3.setText(Integer.toString(Marektodo.allTodo.get(index)
					.getTodoNr()));
		}
		if (insertAdd.equals("I"))
			todoNr.add(row, tb3);
		else
			todoNr.add(tb3);
//		Marektodo.todo.setWidget(row, 2, tb3);

		TextBox ta = new TextBox();
		ta.setStyleName("textArea");
		if (index <= -1) {
			ta.setText("");
		} else {
			ta.setText(Marektodo.allTodo.get(index).getDescription());
		}
		if (insertAdd.equals("I"))
			description.add(row, ta);
		else
			description.add(ta);
		Marektodo.todo.setWidget(row, 2, ta);
		//
		Marektodo.todo.getCellFormatter().addStyleName(row, 0, "addBorderBRL");
		Marektodo.todo.getCellFormatter().addStyleName(row, 1, "addBorderBR");
		Marektodo.todo.getCellFormatter().addStyleName(row, 2, "addBorderBR");
//		Marektodo.todo.getCellFormatter().addStyleName(row, 3, "addBorderBR");
	}

	private void dropDragged() {
//		GWT.log("SEL id="+id.get(rowSel)
//		+" groupNr="+groupNr.get(rowSel)
//		+" title="+title.get(rowSel).getText()
//		+" todoNr="+todoNr.get(rowSel).getText()
//		+" znaczek="+znaczek.get(rowSel).getText()
//		+" style="+style.get(rowSel)
//		+" description="+description.get(rowSel).getText()
//		+" updateFlag="+ updateFlag.get(rowSel));
		
//		GWT.log("DROP start row="+rowNr+" sel="+rowSel);
		if (rowNr > 0) {
			if (groupNr.get(rowNr-1) == groupNr.get(rowNr)
					&& groupNr.get(rowNr) != groupNr.get(rowSel)) {
				Window.alert("You can not to drop in the middle of other cathegorie. ");
				return;
			} 
			if (groupNr.get(rowNr-1) != groupNr.get(rowNr)
					&& groupNr.get(rowNr-1) != groupNr.get(rowSel) 
					&& groupNr.get(rowNr) != groupNr.get(rowSel)){
				if (lineSum > 1) {
					Window.alert("You can not to drop in the middle of other cathegorie. ");
					return;					
				}
			}
		} else {
			if (groupNr.get(rowNr+1) != groupNr.get(rowSel) && lineSum > 1) {
				Window.alert("You can not to drop in the middle of other cathegorie. ");
			return;
			}	
		}
		groupRec = true;
		rf = Marektodo.todo.getRowFormatter();
		rf.removeStyleName(rowSel, "wybrany");		
		// Drop
		Marektodo.todo.insertRow(rowNr);
		id.add(rowNr, id.get(rowSel));
		groupNr.add(rowNr, groupNr.get(rowSel));
		title.add(rowNr, title.get(rowSel));
		todoNr.add(rowNr, todoNr.get(rowSel));
		znaczek.add(rowNr, znaczek.get(rowSel));
		style.add(rowNr, style.get(rowSel));
		description.add(rowNr, description.get(rowSel));
		updateFlag.add(rowNr, updateFlag.get(rowSel));

//		Window.alert("INS row="+rowNr+" id="+id.get(rowNr)
//		+" groupNr="+groupNr.get(rowNr)
//		+" title="+title.get(rowNr).getText()
//		+" todoNr="+todoNr.get(rowNr).getText()
//		+" znaczek="+znaczek.get(rowNr).getText()
//		+" style="+style.get(rowNr)
//		+" description="+description.get(rowNr).getText()
//		+" updateFlag="+ updateFlag.get(rowNr));

		if(rowNr == 0) {
			if(title.get(rowNr+1).getText().equals(title.get(rowNr).getText())) {
				Marektodo.todo.setWidget(rowNr, 0, title.get(rowNr));
				Marektodo.todo.setWidget(rowNr+1, 0, null);
			} else {
				Marektodo.todo.setWidget(rowNr, 0, title.get(rowNr));
			}
		} else {
			if(groupNr.get(rowNr+1) == groupNr.get(rowNr)) {
				if( groupNr.get(rowNr-1) != groupNr.get(rowNr)) {
					Marektodo.todo.setWidget(rowNr, 0, title.get(rowNr));
					Marektodo.todo.setWidget(rowNr+1, 0, null);
				}
			}
			if(groupNr.get(rowNr+1) != groupNr.get(rowNr)) {
				if(groupNr.get(rowNr-1) != groupNr.get(rowNr)) {
					Marektodo.todo.setWidget(rowNr, 0, title.get(rowNr));
				}
			}
		} 
		Marektodo.todo.setWidget(rowNr, 1, znaczek.get(rowNr));
		Marektodo.todo.setWidget(rowNr, 2, description.get(rowNr));
		Marektodo.todo.getCellFormatter().addStyleName(rowNr, 0, "addBorderBRL");
		Marektodo.todo.getCellFormatter().addStyleName(rowNr, 1, "addBorderBR");
		Marektodo.todo.getCellFormatter().addStyleName(rowNr, 2, "addBorderBR");
		rf.setStyleName(rowNr, "tableRow");

		// Remove
		if (rowSel > rowNr) rowSel++;
		else rowNr--;
		removeFromList(rowSel);
		Marektodo.todo.removeRow(rowSel);
		rowSel = -1;
//		GWT.log("DROP end row="+rowNr+" sel="+rowSel);
	}

	private void sendData() {
		userMailService.sendMail(emailAddress.getText().toString(), todoSTR,
				"", "", new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}

					public void onSuccess(String result) {
						String message = result;
						Window.alert(message);
					}
				});

	}
}
