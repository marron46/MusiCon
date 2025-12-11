package config;

/**
 * データベース接続設定を管理する定数クラス
 */
public class DatabaseConfig {
	
	/** データベース接続URL */
	public static final String JDBC_URL = "jdbc:mysql://localhost/musicon";
	
	/** データベースユーザー名 */
	public static final String DB_USER = "root";
	
	/** データベースパスワード */
	public static final String DB_PASS = "";
	
	// インスタンス化を防ぐ
	private DatabaseConfig() {
		throw new AssertionError("このクラスはインスタンス化できません");
	}
}
