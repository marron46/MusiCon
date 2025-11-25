package model.logic;


public class CreateUserLogic {

	//新規ユーザー登録
	public boolean execute(User user //(仮) Servlet で作ったインスタンス) {
		
		//DAOインスタンス生成
		UserDAO userdao = new UserDAO();
	
		//DAOメソッド呼び出し・登録実行	
		if(dao.(//メソッド名)) {
			return true;
		}

		//登録失敗時
		return false;
		
	}
}
