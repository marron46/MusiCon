package service;

import dao.UserDAO;
import model.User;

/**
 * ユーザーログインに関するビジネスロジックを提供するサービスクラス
 */
public class LoginUserService {

	/**
	 * ログインを実行
	 * @param user ユーザー情報
	 * @return ログイン成功時はUserオブジェクト、失敗時はnull
	 */
	public User execute(User user) {
		String userName = user.getUserName();
		String password = user.getUserPass();

		UserDAO dao = new UserDAO();

		if (dao.executeLogin(userName, password) != null) {
			return user;
		}
		return null;
	}
}
