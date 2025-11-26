package model.logic;

import dao.UserDAO;

public class DeleteUserLogic {	
	
	//既存ユーザー削除
	public boolean execute (User user) {

		//DAOインスタンスを生成
		UserDAO dao = new UserDAO();
		
		//DAOメソッド呼び出し・削除実行
		if(dao./*メソッド名*/(user)) { 
			// 削除成功
			return true;
		}		
		// 削除失敗
		return false;
	}

}
