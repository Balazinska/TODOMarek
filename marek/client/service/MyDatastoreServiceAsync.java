/*
 *  @author Anna Balazinska  (abalazinska@hotmail.com)
*/
package marek.client.service;

import java.util.List;

import marek.client.model.Admin;
import marek.client.model.LoginInfo;
import marek.client.model.Todo;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface MyDatastoreServiceAsync {
	void loginSelect(AsyncCallback<List<LoginInfo>> async)
		throws IllegalArgumentException;

	void loginInfoAdd(LoginInfo loginInfo, AsyncCallback<String> callback)
		throws IllegalArgumentException;

	void loginInfoDelete(String nickname, AsyncCallback<String> callback)
		throws IllegalArgumentException;

	void loginInfoUpdate(LoginInfo loginInfo, AsyncCallback<String> callback)
		throws IllegalArgumentException;

	void loginInfoUpdateAll(List<LoginInfo> loginInfo, AsyncCallback<String> callback) 
			throws IllegalArgumentException;

	// Todo
	void todoSelect(String filter, AsyncCallback<List<Todo>> callback) 
			throws IllegalArgumentException;

	void todoInString(AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void todoAdd(Todo todo, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void todoDelete(String id, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void todoUpdate(Todo todo, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void updateAll (List<Todo> updateList, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void todoSearch(String searchString, String sortField, AsyncCallback<List<Todo>> callback)
			throws IllegalArgumentException;

	// Admin
	void adminSelect(String message, AsyncCallback<Admin> callback) 
			throws IllegalArgumentException;

	void adminUpdate(Admin admin, AsyncCallback<String> callback)throws IllegalArgumentException;


}
