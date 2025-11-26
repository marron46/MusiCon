package model.logic;

import dao.UserDAO;

public class LoginUserLogic {

	//ログイン機能
	public boolean execute(User user) {
		//DAOインスタンス生成
		UserDAO dao = new UserDAO();
		if(dao./*メソッド名*/(user)) {
			//ログイン成功
			return true;
		}
		//ログイン失敗
		return false;
	}
}
