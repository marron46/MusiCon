package model;

public class User {//ユーザー情報格納
	private String user_name;//ユーザー名
	private String user_pass;//パスワード
	private int user_id;//ユーザーID
	
	/*コンストラクタ（JavaBeansで必要）
    public User() {}*/

    // コンストラクタ : フィールド設定
	public User(){}
	public User(String user_name,String user_pass){
		this.user_name = user_name;
		this.user_pass = user_pass;
	}
	public User(String user_name,String user_pass,int user_id){
		this.user_name = user_name;
		this.user_pass = user_pass;
		this.user_id = user_id;
	}
	public String getUserName() { return user_name; }
	public String getUserPass() { return user_pass; }
	public int getUserId() { return user_id; }
}
