package marek.client;

import java.util.ArrayList;

import marek.client.model.LoginInfo;
import marek.client.service.MyDatastoreService;
import marek.client.service.MyDatastoreServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class UserAdmin {
	// Buttons - function
	final Button newButton = new Button("Add");
	final Button deleteButton = new Button("Delete");
	//
	final Button saveButton = new Button("Save");
	final Button cancelButton = new Button("Cancel");
	//
	static ArrayList<TextBox> nickname = new ArrayList<TextBox>();
	static ArrayList<TextBox> emailAddress = new ArrayList<TextBox>();
	static ArrayList<TextBox> password = new ArrayList<TextBox>();
	static ArrayList<TextBox> code = new ArrayList<TextBox>();
	static ArrayList<TextBox> remember = new ArrayList<TextBox>();
	static ArrayList<String> updateFlag = new ArrayList<String>();
	//
	private static int rowNr = -1;

	private static HTMLTable.RowFormatter rf;
	private final MyDatastoreServiceAsync myDatastoreService = GWT
			.create(MyDatastoreService.class);

	public HorizontalPanel setMenus() {
		HorizontalPanel menu = new HorizontalPanel();
		//
		newButton.addStyleName("functionButton");
		newButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rowNr = -1;
				Marektodo.users.insertRow(1);
				addOneRow(1, -1, "I");
			}
		});
		deleteButton.addStyleName("functionButton");
		deleteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (rowNr <= 0) {
					Window.alert("Nothing to delete");
					return;
				}
				if (updateFlag.get(rowNr-1).equals("I")) {
					removeFromList();
					Marektodo.allUsers.remove(rowNr-1);
					rowNr = -1;
				} else {
					Boolean yesNo = Window.confirm("Do you really want to delete this user? No undo");
					if (yesNo) {
						Window.alert("row="+(rowNr-1)+" name="+nickname.get(rowNr-1).getText());
						myDatastoreService.loginInfoDelete(nickname.get(rowNr-1).getText(),
								new AsyncCallback<String>() {
							public void onFailure(Throwable error) {
								Window.alert(error.getMessage());
							}

							public void onSuccess(String result) {
								Marektodo redisp = new Marektodo();
								redisp.selectUser();
							}
						});
					}

				}
			}
		});
		saveButton.addStyleName("functionButton");
		saveButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ArrayList<LoginInfo> updList = new ArrayList<LoginInfo>();
				for (int i = 0; i < updateFlag.size(); i++) {
					if (updateFlag.get(i).isEmpty())
						continue;
					LoginInfo rec = new LoginInfo();
					rec.setNickname(nickname.get(i).getText());
					rec.setEmailAddress(emailAddress.get(i).getText());
					rec.setPassword(password.get(i).getText());
					rec.setCode(code.get(i).getText());
					rec.setRemember(remember.get(i).getText());
					rec.setUpdateFlag(updateFlag.get(i));	
					updList.add(rec);
				}
				if (updList.size() == 0) {
					Window.alert("Nothing to save");
					return;
				}
				myDatastoreService.loginInfoUpdateAll(updList,
						new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}

					public void onSuccess(String result) {
						if (!result.equals("OK")) {
							Window.alert(result);
						} else {
							Window.alert("Save success");
							Marektodo redisp = new Marektodo();
							redisp.selectUser();
						}
					}
				});
			}	
		});	
		//
		cancelButton.addStyleName("functionButton");
		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Marektodo user = new Marektodo();
				user.selectUser();
			}
		});
	
		menu.setSpacing(5);	
		menu.setStyleName("paddingBottom");
		menu.add(newButton);
		menu.add(deleteButton);
		menu.add(saveButton);
		menu.add(cancelButton);	
			
		return menu;
	}	
	
	public void userDisplay() {
		//
		initArrays();
		Marektodo.users = new FlexTable();
		Marektodo.users.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				com.google.gwt.user.client.ui.HTMLTable.Cell cell = Marektodo.users
						.getCellForEvent(event);
				rf = Marektodo.users.getRowFormatter();
				if (rowNr > 0) {
					rf.removeStyleName(rowNr, "tableRow");
				}
				rowNr = cell.getRowIndex();
				if (rowNr > 0) {
					rf.setStyleName(rowNr, "tableRow");
					Marektodo.users.getRowFormatter().addStyleName(rowNr, "height");
					if (updateFlag.get(rowNr-1).isEmpty()) {
						updateFlag.set(rowNr-1, "U");
					}
				}
			}
		});
		//
		Marektodo.users.setCellPadding(1);
		Marektodo.users.setCellSpacing(2);
		Marektodo.users.setStyleName("table");
		Marektodo.users.addStyleName("addBorderTLRB");
		// Header
		Marektodo.users.setText(0, 0, "Nickname");
		Marektodo.users.setText(0, 1, "Login");
		Marektodo.users.setText(0, 2, "Password");
		Marektodo.users.setText(0, 3, "Code");
		Marektodo.users.setText(0, 4, "Remember");
		//
		Marektodo.users.getRowFormatter().addStyleName(0, "tableHeader");
		for (int i = 0; i <= 4; i++) {
		Marektodo.users.getCellFormatter().setHorizontalAlignment(0, i,
				HasHorizontalAlignment.ALIGN_CENTER);
		}
		
		if (Marektodo.allUsers.size() == 0) {
			addOneRow(0, -1, "I");
		} else {
			for (int i = 0; i < Marektodo.allUsers.size(); i++) {
				addOneRow(i+1, i, "A");
			}
		}
	}
	
	private void initArrays() {
		nickname = new ArrayList<TextBox>();
		emailAddress = new ArrayList<TextBox>();
		password = new ArrayList<TextBox>();
		code = new ArrayList<TextBox>();
		remember = new ArrayList<TextBox>();
		updateFlag = new ArrayList<String>();
	}
	
	private void removeFromList() {
		nickname.remove(rowNr-1);
		emailAddress.remove(rowNr-1);
		password.remove(rowNr-1);
		code.remove(rowNr-1);
		remember.remove(rowNr-1);
		updateFlag.remove(rowNr-1);
	}
	//
	private void addOneRow(int row, int index, String insertAdd) {
		//
		//	Nickname
		TextBox tb1 = new TextBox();
		tb1.setStyleName("codeBox200");
		if (index <= -1) {
			tb1.setText("");
		} else {
			tb1.setText(Marektodo.allUsers.get(index).getNickname());
		}
		if (insertAdd.equals("I"))
			nickname.add(0, tb1);
		else
			nickname.add(tb1);
		Marektodo.users.setWidget(row, 0, tb1);
		//	Login
		TextBox tb2 = new TextBox();
		tb2.setStyleName("codeBox200");
		if (index <= -1) {
			tb2.setText("");
		} else {
			tb2.setText(Marektodo.allUsers.get(index).getEmailAddress());
		}
		if (insertAdd.equals("I"))
			emailAddress.add(0, tb2);
		else
			emailAddress.add(tb2);
		Marektodo.users.setWidget(row, 1, tb2);
		//	Password
		TextBox tb3 = new TextBox();
		tb3.setStyleName("codeBox200");
		if (index <= -1) {
			tb3.setText("");
		} else {
			tb3.setText(Marektodo.allUsers.get(index).getPassword());
		}
		if (insertAdd.equals("I"))
			password.add(0, tb3);
		else
			password.add(tb3);
		Marektodo.users.setWidget(row, 2, tb3);
		//	Code
		TextBox tb4 = new TextBox();
		tb4.setStyleName("codeBox70");
		if (index <= -1) {
			tb4.setText("");
		} else {
			tb4.setText(Marektodo.allUsers.get(index).getCode());
		}
		if (insertAdd.equals("I"))
			code.add(0, tb4);
		else
			code.add(tb4);
		Marektodo.users.setWidget(row, 3, tb4);
		//	Remember
		TextBox tb5 = new TextBox();
		tb5.setStyleName("codeBox70");
		if (index <= -1) {
			tb5.setText("");
		} else {
			tb5.setText(Marektodo.allUsers.get(index).getRemember());
		}
		if (insertAdd.equals("I"))
			remember.add(0, tb5);
		else
			remember.add(tb5);
		Marektodo.users.setWidget(row, 4, tb5);
		//	UpdateFlag - not displayed
		String upd = "";
		if (index <= -1) {
			upd = "I";
		}
		if (insertAdd.equals("I")) {
			updateFlag.add(0, upd);
		} else {
			updateFlag.add(upd);
		}
		Marektodo.users.getRowFormatter().addStyleName(row, "height");
		for(int j=0; j<=3; j++) {
			Marektodo.users.getCellFormatter().addStyleName(row, j, "addBorderTR");
		}
		Marektodo.users.getCellFormatter().addStyleName(row, 4, "addBorderT");
	}
}
