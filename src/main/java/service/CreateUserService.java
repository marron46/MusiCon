package service;

import dao.UserDAO;
import model.User;

/**
 * ユーザー作成に関するビジネスロジックを提供するサービスクラス
 */
public class CreateUserService {

	/**
	 * 新規ユーザーを登録
	 * @param user ユーザー情報
	 * @return 登録成功時はtrue、失敗時はfalse
	 */
	public boolean execute(User user) {
		UserDAO dao = new UserDAO();
		return dao.registerUser(user);
	}
}
