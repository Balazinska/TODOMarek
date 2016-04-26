/*
 *  @author Anna Balazinska  (abalazinska@hotmail.com)
*/
package marek.client;

import marek.client.model.LoginInfo;
import marek.client.service.MyDatastoreService;
import marek.client.service.MyDatastoreServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import marek.client.dialog.*;
import marek.client.model.*;
import marek.client.table.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Marektodo implements EntryPoint {
	//******************************** Date
	final Label dateTime = new Label();
	String dateNow;
	DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy/MM/dd");
	final HorizontalPanel datePanel = new HorizontalPanel();
	//
	// *******************************Main panel
	static VerticalPanel mainPanel = new VerticalPanel();
	//
	// ******************************Login
	static LoginTable userTable;
	static List<LoginInfo> loginInfo;
	static LoginInfo currentUser;
	LoginInfo loginRec = new LoginInfo();
	Boolean newPassword = false;
	final Label loginNameL = new Label("Name:");
	final Label passwordL = new Label("Password:");
	final TextBox loginName = new TextBox();
	final PasswordTextBox password = new PasswordTextBox();
	final Button loginB = new Button("Login");
	final Button changePasB = new Button("Change password");
	final DialogBox loginDialog = new DialogBox();
	final Grid grid = new Grid(3, 2);
	final VerticalPanel loginP = new VerticalPanel();
	//
	// ***************************Buttons - main menu
	final VerticalPanel mainMenu = new VerticalPanel();
	final Button userButton = new Button("Users");
	final Button todoButton = new Button("To do");
	//
	//******************************User
	public static HorizontalPanel userMenu = new HorizontalPanel();
    public static FlexTable users = new FlexTable();
    public static List<LoginInfo> allUsers = new ArrayList<LoginInfo>();
	// *****************************TODO Data
	public static HorizontalPanel todoMenu = new HorizontalPanel();
	public static FlexTable todoH = new FlexTable();
	public static DoubleClickTable todo = new DoubleClickTable();
	public static Admin adminRec = new Admin();
	public static List<Todo> allTodo = new ArrayList<Todo>();
	public static List<String> titles = new ArrayList<String>();
	public static List<Integer> titleNr = new ArrayList<Integer>();
	public static List<Integer> maxTodoNr = new ArrayList<Integer>();
	//
	int rowNr = -1;
	private static String mode = "TODO";
	//
	private final MyDatastoreServiceAsync myDatastoreService = GWT.create(MyDatastoreService.class);

	MyDatastoreServiceAsync service = (MyDatastoreServiceAsync) GWT
			.create(MyDatastoreService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		//
		// Login
		myDatastoreService.loginSelect(	new AsyncCallback<List<LoginInfo>>() {
					public void onFailure(Throwable error) {
						Window.alert(error.getMessage());
					}

					public void onSuccess(List<LoginInfo> result) {
							loginInfo = result;
//							for(int i=0; i<loginInfo.size();i++)
//								Window.alert(loginInfo.get(i).getNickname()
//								 +" "+loginInfo.get(i).getPassword());
						if (loginInfo.isEmpty()) {
							UserDialog createUser = new UserDialog();
							createUser.userInfo("F", -1, currentUser,
									userTable, loginInfo);
						} else {
							loginDialog();
						}
					}
				});
	}

	private void loginDialog() {
		// Data
		loginNameL.setStyleName("dialogLabel");
		passwordL.setStyleName("dialogLabel");
		loginName.setStyleName("textBox");
		password.setStyleName("textBox");
		// Buttons
		loginB.addStyleName("dataButton");
		loginB.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (checkPassword() == true || newPassword) {
					grid.setVisible(false);
					mainLoad();
				}
			}
		});
		changePasB.addStyleName("dataButton");
		changePasB.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (checkPassword() == true) {
					UserDialog updateUser = new UserDialog();
					updateUser.userInfo("C", -1, currentUser, userTable,loginInfo);
				}
			}
		});

		password.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Boolean found = false;
				if (!loginName.getText().isEmpty()) {
					for (LoginInfo login : loginInfo) {
						if (login.getEmailAddress().equals(
								loginName.getText().toString())) {
							currentUser = login;
							found = true;
							break;
						}
					}
				}
				if (!found) {
					Window.alert("User: "+loginName.getText().toString()+" does not exist");
					return;
				}
				if (!currentUser.emailAddress.isEmpty()
						&& currentUser.getRemember().equals("Y")) {
					password.setText(currentUser.getPassword());
				}
			}
		});

		// Dialog grid
		grid.setWidget(0, 0, loginNameL);
		grid.setWidget(0, 1, loginName);
		grid.setWidget(1, 0, passwordL);
		grid.setWidget(1, 1, password);
		grid.setWidget(2, 0, loginB);
		grid.setWidget(2, 1, changePasB);

		loginP.add(grid);
		RootPanel.get("loginPanelContainer").add(loginP);	
	}

	private void mainLoad() {
		//*********************New datastore id for TODO
		service.adminSelect("", new AsyncCallback<Admin>() {
			public void onFailure(Throwable error) {
				Window.alert(error.getMessage());
			}

			public void onSuccess(Admin result) {
				adminRec = result;
			}

		});
		//**********************Create menus
		TodoAdmin todoA = new TodoAdmin();
		todoMenu = todoA.setMenus();
		UserAdmin userA = new UserAdmin();
		userMenu = userA.setMenus();
		//**********************Main menu : Users, Todos butoons
		//
		mainMenu.setSpacing(10);
		mainMenu.add(datePanel);
		//
		// "User button is visible only for Admin user
		userButton.addStyleName("menuItem");
		if (currentUser.getCode().equals("A")) {
			userButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
	//				clearScreen();
					//Window.alert("USER go to select");
					selectUser();
				}
			});
			mainMenu.add(userButton);
		}
		//
		// TODO button
		todoButton.addStyleName("menuItem");
		todoButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				clearScreen();
				//Window.alert("TODO go to select");
				selectTodo("");
			}
		});
		mainMenu.add(todoButton);
		//
		// ********************************* Date
		dateNow = dtf.format(new Date());
		dateTime.addStyleName("timeStyle");
		dateTime.setText(DateTimeFormat.getFormat("yyyy/MM/dd HH:mm").format(
				new Date()));
		datePanel.add(dateTime);
		datePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		//
		//
		// TODO
		selectTodo("");
		//
		// ********************************* Root panel
		mainPanel.setSpacing(0);
		RootPanel.get("mainPanelContainer").add(datePanel);
		RootPanel.get("mainPanelContainer").add(mainPanel);
		RootPanel.get("menuPanelContainer").add(mainMenu);
	}
	// ************************ Display methods
	private void clearScreen() {
		//Window.alert("mode="+mode);
		if(mode.equals("TODO")) {
			mainPanel.remove(todoMenu);
			mainPanel.remove(todoH);
			mainPanel.remove(todo);
			todoButton.setStyleName("menuItem");
		}
		if(mode.equals("USER")) {
			mainPanel.remove(userMenu);
			mainPanel.remove(users);
			userButton.setStyleName("menuItem");
		}
	}
	// **********************Todo
	public void selectTodo(final String filter) {
		clearScreen();
		mode = "TODO";
		allTodo = new ArrayList<Todo>();

		service.todoSelect(filter, new AsyncCallback<List<Todo>>() {
			public void onFailure(Throwable error) {
				Window.alert(error.getMessage());
			}

			public void onSuccess(List<Todo> result) {
				allTodo = result;
				if (filter.isEmpty())
					findTitles();
				displayTodo();
			}
		});
	}
	//
	private void findTitles() {
		Boolean done = false;
		titles = new ArrayList<String>();
		titleNr = new ArrayList<Integer>();
		maxTodoNr = new ArrayList<Integer>();
		for (int i=0; i<allTodo.size(); i++) {
			done = false;
			for (int j=0; j<titles.size(); j++) {
				if (titles.get(j).toUpperCase().equals(allTodo.get(i).getTitle().toUpperCase())) {
					done = true;
					break;
				}
			}
			if (done) 
				continue;
			String title = allTodo.get(i).getTitle().toUpperCase(); 
			titles.add(allTodo.get(i).getTitle());
			titleNr.add(allTodo.get(i).getGroupNr());
			int prevTodo = 1;
			for (int j=0; j<titles.size(); j++) {
				if (!titles.get(j).toUpperCase().equals(title))
					continue;
				if (prevTodo < allTodo.get(j).getTodoNr())
					prevTodo = allTodo.get(j).getTodoNr();
			}
			maxTodoNr.add(prevTodo);
		}
//		for(int i=0; i<titles.size(); i++) {
//			Window.alert("T="+titles.get(i)+" gr="+titleNr.get(i)+" to="+maxTodoNr);
//		}
		
	}
//
	private void displayTodo() {
		mode = "TODO";
		todoButton.setStyleName("menuActiveItem");
		TodoAdmin disp = new TodoAdmin();
		disp.todoDisplay();
		mainPanel.add(todoMenu);
		mainPanel.add(todoH);
		mainPanel.add(todo);
		RootPanel.get("mainPanelContainer").add(mainPanel);
	}
	

	public void selectUser() {
		clearScreen();
		allUsers = new ArrayList<LoginInfo>();
		service.loginSelect(new AsyncCallback<List<LoginInfo>>() {
			public void onFailure(Throwable error) {
				Window.alert(error.getMessage());
			}

			public void onSuccess(List<LoginInfo> result) {
//				clearScreen();
				mode = "USER";
				allUsers = result;
				displayUsers();
			}
		});
	}

	private void displayUsers() {
		userButton.setStyleName("menuActiveItem");
		UserAdmin disp = new UserAdmin();
		disp.userDisplay();
		mainPanel.add(userMenu);
		mainPanel.add(users);
		RootPanel.get("mainPanelContainer").add(mainPanel);
	}
	
	// *************************** Login
	private Boolean checkPassword() {
		Boolean found = false;
		for (LoginInfo login : loginInfo) {
			if (login.getEmailAddress().equals(loginName.getText().toString())) {
				if (password.getText().toString().equals(login.getPassword())) {
					currentUser = login;
					found = true;
					break;
				}
			}
		}
		if (found == true) {
			return true;
		} else {
			Window.alert("Password is not correct.");
			return false;
		}
	}
	
	public void setUser(LoginInfo user, String mode) {
		newPassword = true;
		currentUser = user;
		Window.alert("new user="+currentUser.getNickname());
		if (mode.equals("F")) {
			mainLoad();
		}
	}

}
