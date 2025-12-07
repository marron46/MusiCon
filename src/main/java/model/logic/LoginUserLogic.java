package model.logic;

import dao.UserDAO;
import model.User;

public class LoginUserLogic {

	public User execute(User user) {

		// User オブジェクトからパスワードと名前を取得
		String nm = user.getUserName();
		String pw = user.getUserPass();

		//DAOインスタンス生成
		UserDAO dao = new UserDAO();

		if (dao.executeLogin(nm, pw) != null) {
			//ログイン成功
			return user;
		}
		//ログイン失敗
		return null;
	}
}