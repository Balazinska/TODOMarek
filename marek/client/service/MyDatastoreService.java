/*
 *  @author Anna Balazinska  (abalazinska@hotmail.com)
*/
package marek.client.service;

import java.util.List;

import marek.client.model.Admin;
import marek.client.model.LoginInfo;
import marek.client.model.Todo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("datastoreService")
public interface MyDatastoreService extends RemoteService {
	List<LoginInfo> loginSelect();
	String loginInfoAdd(LoginInfo loginInfo);
	String loginInfoDelete(String nickname);
	String loginInfoUpdate(LoginInfo loginInfo);
	String loginInfoUpdateAll(List<LoginInfo> loginInfo);
	// TODO
	List<Todo> todoSelect(String filter);
	String todoInString();
	String todoAdd(Todo todo);
	String todoDelete(String id);
	String todoUpdate(Todo todo);
	String updateAll (List<Todo> updateList);
	List<Todo> todoSearch(String searchString, String sortField);
	// Admin
	Admin adminSelect(String message);
	String adminUpdate(Admin admin);

}
