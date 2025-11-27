package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

public class UserDAO {

	private final String JDBC_URL = "jdbc:mysql://localhost/musicon";
	private final String DB_USER = "root";
	private final String DB_PASS = "";

	public boolean executeLogin(String nm, String pw) {
		// JDBCドライバを読み込む
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}

		// データベース接続
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {

			// SELECT文の準備（ユーザー名とパスワードでユーザーを確認）
			String sql = "SELECT * FROM USERS WHERE USER_NAME=? AND USER_PASS=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, nm);
			pStmt.setString(2, pw);

			// SELECT文を実行
			ResultSet rs = pStmt.executeQuery();

			// 結果が存在すればユーザーを確認
			if (rs.next()) {
				if (rs.getString("USER_PASS").equals(pw) && rs.getString("USER_NAME").equals(nm)) {
					System.out.println("Success : UserDAO.executeLogin");
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // SQLエラーを表示
			System.out.println("Error : UserDAO.executeLogin");
			return false;
		}
		return false; // ユーザー確認失敗
	}

	public boolean registerUser(User user) {
		// JDBCドライバを読み込む
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}

		// データベース接続
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {

			String nm = user.getUserName();
			String pw = user.getUserPass();

			// INSERT文の準備（新規ユーザーをデータベースに登録）
			String sql = "INSERT INTO USERS(USER_NAME, USER_PASS) VALUES (?, ?)";
			PreparedStatement pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, nm);
			pStmt.setString(2, pw);

			// INSERT文を実行
			int result = pStmt.executeUpdate();

			// 登録成功か確認
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace(); // SQLエラーを表示
			System.out.println("Error : UserDAO.registerUser");
			return false; // 失敗
		}
	}

	public boolean deleteUser(User user) {
		// JDBCドライバを読み込む
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}

		// データベース接続
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
			
			String nm = user.getUserName();
			String pw = user.getUserPass();
			
			// UPDATE文の準備（指定したユーザー名またはパスワードでユーザーを削除）
			String sql = "UPDATE USERS SET IS_DELETED = 1 WHERE USER_NAME=? AND USER_PASS=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, nm);
			pStmt.setString(2, pw);
			// UPDATE文を実行
			int delete = pStmt.executeUpdate();

			// 削除成功か確認
			return delete > 0; // 1行以上削除されれば成功

		} catch (SQLException e) {
			e.printStackTrace(); // SQLエラーを表示
			System.out.println("Error : UserDAO.deleteUser");
			return false; // 失敗
		}
	}
}
