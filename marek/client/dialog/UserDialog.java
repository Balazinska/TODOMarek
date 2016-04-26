/*
 *  @author Anna Balazinska  (abalazinska@hotmail.com)
*/
package marek.client.dialog;

import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import marek.client.Marektodo;
import marek.client.model.LoginInfo;
import marek.client.service.MyDatastoreService;
import marek.client.service.MyDatastoreServiceAsync;
import marek.client.table.LoginTable;

public class UserDialog {

	// Labels
	final Label loginL = new Label("Login: ");
	final Label nicknameL = new Label("Nickname: ");
	final Label userCodeL = new Label("User code ('A' or blank): ");
	final Label oldPasswordL = new Label("Old password: ");
	final Label passwordL = new Label("Password: ");
	final Label confirmL = new Label("Confirm password: ");
	final Label rememberL = new Label("Remember me:");
	// Data
	LoginInfo currUser = new LoginInfo();
	final TextBox loginName = new TextBox();
	final TextBox nickname = new TextBox();
	final TextBox userCode = new TextBox();
	final PasswordTextBox oldPassword = new PasswordTextBox();
	final PasswordTextBox password = new PasswordTextBox();
	final PasswordTextBox confirm = new PasswordTextBox();
	final TextBox remember = new TextBox();
	// Button
	final Button save = new Button("Save");
	final Button cancel = new Button("Cancel");

	final DialogBox userInfo = new DialogBox();
	final Grid grid = new Grid(7, 2);
	final VerticalPanel dialogP = new VerticalPanel();

	private final MyDatastoreServiceAsync loginService = GWT
			.create(MyDatastoreService.class);

	public void userInfo(final String mode, final int row,
			final LoginInfo login, final LoginTable loginTable,
			final List<LoginInfo> loginList) {
		//
		//
		// Initialise the dialog box and data for update
		if (mode.equals("F")) {
			userInfo.setText("First user");
			userCode.setText("A");
		}
		if (mode.equals("C") || mode.equals("U")) {
			if (mode.equals("C")) {
				userInfo.setText("Change password");
				grid.resize(4, 2);
				currUser.setEmailAddress(login.getEmailAddress());
				currUser.setNickname(login.getNickname());
				currUser.setCode(login.getCode());
				currUser.setRemember(login.getRemember());
			} else {
				userInfo.setText("Update user info");
				loginName.setText(login.getEmailAddress());
				nickname.setText(login.getNickname());
				userCode.setText(login.getCode());
				remember.setText(login.getRemember());
			}
		}

		if (mode.equals("A")) {
			userInfo.setText("Add user");
		}
		userInfo.setAnimationEnabled(true);
		userInfo.addStyleName("dialogBox");
		userInfo.setPopupPosition(100, 150);

		// Labels & data boxes
		if (!mode.equals("C")) {
			nicknameL.addStyleName("primarykeyL");
			loginL.addStyleName("dialogLabel");
			userCodeL.addStyleName("dialogLabel");
			nickname.addStyleName("primarykey");
			loginName.addStyleName("textBox");
			userCode.addStyleName("code");
			rememberL.addStyleName("dialogLabel");
			remember.addStyleName("code");
		} else {
			oldPasswordL.addStyleName("dialogLabel");
			oldPassword.addStyleName("textBox");
		}
		passwordL.addStyleName("dialogLabel");
		confirmL.addStyleName("dialogLabel");
		password.addStyleName("textBox");
		confirm.addStyleName("textBox");

		// Buttons
		save.addStyleName("dataButton");
		save.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Validate data
				if (!userCode.getText().toString().toUpperCase().equals("A")
						&& !userCode.getText().isEmpty()) {
					userCode.setText("");
					userCode.setFocus(true);
					Window.alert("User code has to be equal 'A' or has to be blank");
					return;
				}
				if (!remember.getText().toString().toUpperCase().equals("Y")
						&& !remember.getText().isEmpty()) {
					remember.setText("");
					remember.setFocus(true);
					Window.alert("Remeber has to be equal 'Y' or has to be blank");
					return;
				}

				// Validate password
				if (mode.equals("C") || mode.equals("F") || mode.equals("A")) {
					if (mode.equals("C")) {
						if (!oldPassword.getText().toString()
								.equals(login.getPassword())) {
							Window.alert("Old password is not correct");
							oldPassword.setFocus(true);
							return;
						}
					}
					if (!password.getText().equals(confirm.getText())) {
						confirm.setText("");
						confirm.setFocus(true);
						Window.alert("Password does not match with confirm.");
						password.setFocus(true);
						return;
					}
				}
				// Set data
				if (!mode.equals("C")) {
					currUser.setEmailAddress(loginName.getText());
					currUser.setNickname(nickname.getText());
					currUser.setCode(userCode.getText().toUpperCase());
					currUser.setRemember(remember.getText().toUpperCase());
					if (!mode.equals("U")) {
						currUser.setPassword(password.getText());
					} else {
						currUser.setPassword(login.getPassword());
					}
				} else {
					currUser.setPassword(password.getText());
				}
				// Save data
				// UPDATE
				if (mode.equals("C") || mode.equals("U")) {
					loginService.loginInfoUpdate(currUser,
							new AsyncCallback<String>() {
								public void onFailure(Throwable caught) {
									Window.alert(caught.getMessage());
								}

								public void onSuccess(String result) {
									userInfo.hide();
									loginTable.setText(row, 0,
											currUser.getNickname());
									loginTable.setText(row, 1,
											currUser.getEmailAddress());
									loginTable.setText(row, 2,
											currUser.getPassword());
									loginTable.setText(row, 3,
											currUser.getCode());
									loginTable.setText(row, 4,
											currUser.getRemember());
									//
									Marektodo onLoad = new Marektodo();
									onLoad.setUser(currUser, mode);
								}
							});
				}
				// INSERT
				else {
					// currUser.setPassword(password.getText());
					loginService.loginInfoAdd(currUser,
							new AsyncCallback<String>() {
								public void onFailure(Throwable caught) {
									Window.alert(caught.getMessage());
								}

								public void onSuccess(String result) {
									userInfo.hide();
									if (mode.equals("A")) {
										int rowAdd = loginTable.getRowCount();
										if (rowAdd > 0) {
											rowAdd = loginTable
													.insertRow(rowAdd);
										}
										loginTable.setText(rowAdd, 0,
												currUser.getNickname());
										loginTable.setText(rowAdd, 1,
												currUser.getEmailAddress());
										loginTable.setText(rowAdd, 2,
												currUser.getPassword());
										loginTable.setText(rowAdd, 3,
												currUser.getCode());
										loginTable.setText(rowAdd, 4,
												currUser.getRemember());
										loginList.add(currUser);
									}
									Marektodo onLoad = new Marektodo();
									onLoad.setUser(currUser, mode);
								}
							});
				}
			}
		});
		//
		cancel.addStyleName("dataButton");
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				userInfo.hide();
			}
		});

		// Dialog grid
		int i = 0;
		if (!mode.equals("C")) {
			grid.setWidget(i, 0, loginL);
			grid.setWidget(i, 1, loginName);
			i = i + 1;
			grid.setWidget(i, 0, nicknameL);
			grid.setWidget(i, 1, nickname);
			i = i + 1;
			grid.setWidget(i, 0, userCodeL);
			grid.setWidget(i, 1, userCode);
			i = i + 1;
		}
		if (mode.equals("C")) {
			grid.setWidget(i, 0, oldPasswordL);
			grid.setWidget(i, 1, oldPassword);
			i = i + 1;
		}
		if (!mode.equals("U")) {
			grid.setWidget(i, 0, passwordL);
			grid.setWidget(i, 1, password);
			i = i + 1;
			grid.setWidget(i, 0, confirmL);
			grid.setWidget(i, 1, confirm);
			i = i + 1;
		}
		if (!mode.equals("C")) {
			grid.setWidget(i, 0, rememberL);
			grid.setWidget(i, 1, remember);
			i = i + 1;
		}
		grid.setWidget(i, 0, save);
		grid.setWidget(i, 1, cancel);

		dialogP.add(grid);
		userInfo.setWidget(dialogP);
		userInfo.show();
	}
}
