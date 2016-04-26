/*
 *  @author Anna Balazinska  (abalazinska@hotmail.com)
*/
package marek.server;

import java.util.*;
import java.text.*;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import marek.client.model.Admin;
import marek.client.model.LoginInfo;
import marek.client.model.Todo;
import marek.client.service.MyDatastoreService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class MyDatastoreServiceImpl extends RemoteServiceServlet implements
		MyDatastoreService {

	DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
	String dateNow = formatter.format(new Date());

	private List<LoginInfo> loginL = new ArrayList<LoginInfo>();
	private List<Todo> todoL = new ArrayList<Todo>();
	static String todoStr;
	int nbr = 0;

	private static final PersistenceManagerFactory PMF = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

	// *****************************Login*******************************
	public String loginInfoUpdateAll(List<LoginInfo> loginInfo) {
		String returnMessage = new String("OK");
		for (int i =0; i<loginInfo.size(); i++) {
			//System.out.println("upd="+loginInfo.get(i).getUpdateFlag());
			if (loginInfo.get(i).getUpdateFlag().equals("I")) {
				returnMessage = loginInfoAdd(loginInfo.get(i));
			} else {
				returnMessage = loginInfoUpdate(loginInfo.get(i));
			}
			if (!returnMessage.equals("OK"))
				break;
		}
		return returnMessage;
	}
	
	public String loginInfoAdd(LoginInfo loginInfo) {
		//System.out.println("add");
		String returnMessage = new String("OK");
		String nickname = loginInfo.getNickname();
		String emailAddress = loginInfo.getEmailAddress();
		String password = loginInfo.getPassword();
		String code = loginInfo.getCode();
		String remember = loginInfo.getRemember();
		PersistenceManager pm = getPersistenceManager();
		try {
//			System.out.println("INSERT:"+nickname+", "+ emailAddress
//					+", "+ password+", "+ code+", "+ remember);
			pm.makePersistent(new LoginInfoBD(nickname, emailAddress, password,
					code, remember));
		} catch(Exception e) {
			returnMessage = e.toString();
		}
		finally {
			pm.close();
		}
		return returnMessage;
	}

	//
	public String loginInfoDelete(String nickname) {
		String returnMessage = new String("OK");
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(LoginInfoBD.class);
			@SuppressWarnings("unchecked")
			List<LoginInfoBD> loginBD = (List<LoginInfoBD>) q.execute();
			for (LoginInfoBD login : loginBD) {
				//System.out.println("del.: "+nickname+" db="+login.getNickname());
				if (nickname.equals(login.getNickname())) {
					pm.deletePersistent(login);
				}
			}
		} finally {
			pm.close();
		}
		return returnMessage;
	}

	//
	public String loginInfoUpdate(LoginInfo loginInfo) {
		String returnMessage = new String("OK");
		//System.out.println("upd");
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(LoginInfoBD.class);
			@SuppressWarnings("unchecked")
			List<LoginInfoBD> login = (List<LoginInfoBD>) q.execute();
			for (LoginInfoBD loginInfoS : login) {
				if (loginInfo.getNickname().equals(loginInfoS.getNickname())) {
					pm.detachCopy(loginInfoS);
					loginInfoS.setNickname(loginInfo.nickname);
					loginInfoS.setEmailAddress(loginInfo.emailAddress);
					loginInfoS.setPassword(loginInfo.password);
					loginInfoS.setCode(loginInfo.code);
					loginInfoS.setRemember(loginInfo.remember);
					pm.makePersistent(loginInfoS);
					break;
				}
			}
		} finally {
			pm.close();
		}
		return returnMessage;
	}

	//
	public List<LoginInfo> loginSelect() {
		loginL = new ArrayList<LoginInfo>();
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(LoginInfoBD.class);
			@SuppressWarnings("unchecked")
			List<LoginInfoBD> login = (List<LoginInfoBD>) q.execute();
			for (int i = 0; i < login.size(); i++) {
				LoginInfo loginOut = new LoginInfo();
				loginOut.setNickname(login.get(i).getNickname());
				loginOut.setEmailAddress(login.get(i).getEmailAddress());
				loginOut.setPassword(login.get(i).getPassword());
				loginOut.setCode(login.get(i).getCode());
				loginOut.setRemember(login.get(i).getRemember());
				loginL.add(loginOut);
			}
			//System.out.println("USER size="+loginL.size());
		} finally {
			pm.close();
		}
		return loginL;
	}

	// ************************TODO*********************************************
	// Add TODO
	public String todoAdd(Todo todo) {
		String returnMessage = new String("OK");
		// ID update
		Admin newid = new Admin();
		newid = adminSelect(returnMessage);
		newid.setIdTodo(Integer.parseInt(todo.getId()));
		adminUpdate(newid);
		//
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.makePersistent(new TodoBD(todo.getId(), todo.getGroupNr(), todo.getTitle(),
					todo.getTodoNr(),todo.znaczek, todo.getDescription()));
		} finally {
			pm.close();
		}
		return returnMessage;
	}

	// Delete
	public String todoDelete(String id) {
		String returnMessage = new String("OK");
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(TodoBD.class);
			@SuppressWarnings("unchecked")
			List<TodoBD> todoBD = (List<TodoBD>) q.execute();
			for (TodoBD todo : todoBD) {
				if (id.equals(todo.getId())) {
					pm.deletePersistent(todo);
				}
			}
		} finally {
			pm.close();
		}
		return returnMessage;
	}

	// Update
	public String todoUpdate(Todo todo) {
		String returnMessage = new String("OK");
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(TodoBD.class);
			@SuppressWarnings("unchecked")
			List<TodoBD> td = (List<TodoBD>) q.execute();
			for (TodoBD todoS : td) {
				if (todo.getId().equals(todoS.getId())) {
					pm.detachCopy(todoS);
					todoS.setId(todo.id);
					todoS.setGroupNr(todo.groupNr);
					todoS.setTitle(todo.title);
					todoS.setTodoNr(todo.todoNr);
					todoS.setZnaczek(todo.znaczek);
					todoS.setDescription(todo.description);
					pm.makePersistent(todoS);
					break;
				}
			}
		} finally {
			pm.close();
		}
		return returnMessage;
	}

	// Update All
	public String updateAll (List<Todo> updateList) {
		String message = "OK";
		String id = "-1";
		//System.out.println("size="+updateList.size());
		
		for (int i=0; i< updateList.size(); i++) {
			//System.out.println("upd="+updateList.get(i).getUpdateFlag());
			if (updateList.get(i).getUpdateFlag().equals("I")) {
				if (Integer.parseInt(id) < Integer.parseInt(updateList.get(i).getId())) {
					id = updateList.get(i).getId();
				}
				id = updateList.get(i).getId();
				message = todoAdd(updateList.get(i));
			}
			else
				message = todoUpdate(updateList.get(i));
			if(!message.equals("OK"))
				return message;
		}
		if (!id.equals("-1")) {
			Admin rec = new Admin();
			rec.setIdTodo(Integer.parseInt(id));
			adminUpdate(rec);
		}
		return message;
	}
	// Query
	// Bytes array to be sent by email
	public String todoInString() {
		todoStr = "";
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(TodoBD.class);
			@SuppressWarnings("unchecked")
			List<TodoBD> td = (List<TodoBD>) q.execute();

			todoStr = new String("TODO");
			todoStr = todoStr + ";\n\r";
			todoStr = todoStr
					+ "Id |Group Nr| Title| TODO| Description;\n\r";
			for (TodoBD todoS : td) {
				//
				todoStr = todoStr + todoS.getId() + "| "
						+ todoS.getGroupNr() + "| "
						+ todoS.getTitle()	+ "| " 
						+ todoS.getTodoNr() + "| "
						+ todoS.getZnaczek() + "| "
						+ todoS.getDescription() + "|;\n\r";
			}

		} finally {
			pm.close();
		}
		return todoStr;
	}

	// Select for display
	public List<Todo> todoSelect(String filter) {
		todoL = new ArrayList<Todo>();

		PersistenceManager pm = getPersistenceManager();
		try {
				Query q = pm.newQuery(TodoBD.class);
				q.setOrdering("groupNr, todoNr");
	
				@SuppressWarnings("unchecked")
				List<TodoBD> todo = (List<TodoBD>) q.execute();
				for (int i = 0; i < todo.size(); i++) {
					if(!filter.isEmpty()) {
						if(!todo.get(i).getDescription().toUpperCase().contains(filter.toUpperCase()))
							continue;
					}
					Todo todoOut = new Todo();
					todoOut.setId(todo.get(i).getId());
					todoOut.setGroupNr(todo.get(i).getGroupNr());
					todoOut.setTitle(todo.get(i).getTitle());
					todoOut.setTodoNr(todo.get(i).getTodoNr());
					todoOut.setZnaczek(todo.get(i).getZnaczek());
					todoOut.setDescription(todo.get(i).getDescription());
					//
					//
					todoL.add(todoOut);
				}
				//System.out.println("TODO size="+todoL.size());
		} finally {
			pm.close();
		}
		return todoL;
	}

	public List<Todo> todoSearch(String searchString, String sortField) {
		String todoStr = new String();
		String oneRec = new String();

		todoStr = todoInString();
		int i = 0;
		int recIndex = 0;
		todoL = new ArrayList<Todo>();
		while (i < todoStr.length()) {
			recIndex = todoStr.indexOf(13, i);
			oneRec = todoStr.substring(i, recIndex - 1);
			if (oneRec.toUpperCase().lastIndexOf(searchString.toUpperCase()) > -1) {
				//
				Todo todoOut = new Todo();
				int startOfValue = i;
				int endOfValue = todoStr.indexOf(124, startOfValue);
				todoOut.setId(todoStr.substring(startOfValue, endOfValue)
						.trim());
				startOfValue = endOfValue + 1;
				endOfValue = todoStr.indexOf(124, startOfValue);
				todoOut.setGroupNr(Integer.parseInt(todoStr.substring(startOfValue, endOfValue)
						.trim()));
				startOfValue = endOfValue + 1;				
				endOfValue = todoStr.indexOf(124, startOfValue);
				todoOut.setTitle(todoStr.substring(startOfValue, endOfValue)
						.trim());
				startOfValue = endOfValue + 1;
				endOfValue = todoStr.indexOf(124, startOfValue);
				todoOut.setTodoNr(Integer.parseInt(todoStr.substring(startOfValue, endOfValue)
						.trim()));
				startOfValue = endOfValue + 1;
				endOfValue = todoStr.indexOf(124, startOfValue);
				todoOut.setZnaczek(todoStr.substring(startOfValue,
						endOfValue).trim());
				startOfValue = endOfValue + 1;
				endOfValue = todoStr.indexOf(124, startOfValue);
				todoOut.setDescription(todoStr.substring(startOfValue,
						endOfValue).trim());
				todoL.add(todoOut);
			}
			i = recIndex + 1;
		}
		return todoL;
	}

	// ***************************** Admin *********************************
	public Admin adminSelect(String message) {
		Admin adminRec = new Admin();
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(AdminBD.class);
			@SuppressWarnings("unchecked")
			List<AdminBD> oneRec = (List<AdminBD>) q.execute();
			if (oneRec.size() == 0) {
				AdminBD init = new AdminBD(1);
				init.setIdTodo(1);
				pm.makePersistent(init);
				adminRec.setIdTodo(1);
			} else {
				adminRec.setIdTodo(oneRec.get(0).getIdTodo());
			}
		} finally {
			pm.close();
		}
		return adminRec;
	}

	public String adminUpdate(Admin admin) {
		String returnMessage = "OK";
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(AdminBD.class);
			@SuppressWarnings("unchecked")
			List<AdminBD> adminS = (List<AdminBD>) q.execute();
			for (AdminBD oneRec : adminS) {
				pm.detachCopy(oneRec);
				oneRec.setIdAdmin();
				oneRec.setIdTodo(admin.getIdTodo());
				pm.makePersistent(oneRec);
				break;
			}
		} finally {
			pm.close();
		}
		return returnMessage;
	}

}
