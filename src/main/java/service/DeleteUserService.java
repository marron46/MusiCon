package service;

import dao.UserDAO;
import model.User;

/**
 * ユーザー削除に関するビジネスロジックを提供するサービスクラス
 */
public class DeleteUserService {
	
	/**
	 * 既存ユーザーを削除
	 * @param user ユーザー情報
	 * @return 削除成功時はtrue、失敗時はfalse
	 */
	public boolean execute(User user) {
		UserDAO dao = new UserDAO();
		return dao.deleteUser(user);
	}
}
