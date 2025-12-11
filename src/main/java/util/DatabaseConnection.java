package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import config.DatabaseConfig;

/**
 * データベース接続を管理するユーティリティクラス
 */
public class DatabaseConnection {
	
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	
	static {
		// JDBCドライバを一度だけ読み込む
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした", e);
		}
	}
	
	/**
	 * データベース接続を取得する
	 * @return Connection データベース接続
	 * @throws SQLException データベース接続エラー
	 */
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(
			DatabaseConfig.JDBC_URL,
			DatabaseConfig.DB_USER,
			DatabaseConfig.DB_PASS
		);
	}
}
