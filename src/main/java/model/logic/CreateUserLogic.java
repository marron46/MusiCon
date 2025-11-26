package model.logic;

import dao.UserDAO;

public class CreateUserLogic {

	//新規ユーザー登録
	public boolean execute (User user) {
		
		//DAOインスタンス生成
		UserDAO dao = new UserDAO();
	
		//DAOメソッド呼び出し・登録実行	
		if(dao./*メソッド名*/(user)) {
			//登録成功
			return true;
		}
		//登録失敗
		return false;
	}
}
