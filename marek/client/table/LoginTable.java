/*
 *  @author Anna Balazinska  (abalazinska@hotmail.com)
*/
package marek.client.table;

import java.util.List;
import com.google.gwt.user.client.ui.FlexTable;

import marek.client.model.LoginInfo;

public class LoginTable  extends FlexTable {
	LoginDataSource input;
	
	public LoginTable(LoginDataSource input) {
		super();
		this.setCellPadding(1);
		this.setCellSpacing(0);
		this.setWidth("100%");
		this.setBorderWidth(5);
		this.setInput(input);
	}
	
	public void setInput(LoginDataSource input) {
		for (int i = this.getRowCount(); i > 0; i--) {
			this.removeRow(0);
		}
		if (input == null) {
			return;
		}

		int row = 0;
		List<String> headers = input.getTableHeader();
		if (headers != null) {
			int i = 0;
			for (String string : headers) {
				this.setText(row, i, string);
				i++;
			}
			row++;
		}
		// Make the table header look nicer
		this.getRowFormatter().addStyleName(0, "tableHeader");
		
		List<LoginInfo> rows = input.getLoginInfo();
		int i = 1;
		for(LoginInfo login : rows) {
			this.setText(i,0, login.getNickname());
			this.setText(i,1, login.getEmailAddress());
			this.setText(i,2, login.getPassword());
			this.setText(i,3, login.getCode());
			this.setText(i,4, login.getRemember());
			i++;
		}
		this.input = input;
	}

}
