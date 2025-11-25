package model;

public class User {//ユーザー情報格納
	private String user_name;//ユーザー名
	private String user_pass;//パスワード
	
	/*デフォルトコンストラクタ（JavaBeansで必要）
    public User() {}*/

    // コンストラクタ : フィールド設定
	public User(String user_name,String user_pass){
		this.user_name = user_name;
		this.user_pass = user_pass;
	}
	public String getUserName() { return user_name; }
	public String getUserPass() { return user_pass; }
}
