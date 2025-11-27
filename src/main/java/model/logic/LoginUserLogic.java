package model.logic;

import dao.UserDAO;
import model.User;

public class LoginUserLogic {

	//ログイン機能
	public boolean execute(User user) {
		
		// User オブジェクトからパスワードと名前を取得
		String nm = user.getUserName();
		String pw = user.getUserPass();
		
		//DAOインスタンス生成
		UserDAO dao = new UserDAO();
		
		if(dao.executeLogin(nm,pw)) {
			//ログイン成功
			return true;
		}
		//ログイン失敗
		return false;
	}
}