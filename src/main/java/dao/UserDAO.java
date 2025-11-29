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

	// ログインを実行するメソッド
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

			String sql = "SELECT * FROM USERS WHERE USER_NAME=? AND USER_PASS=? AND IS_DELETED=0";

			PreparedStatement pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, nm);
			pStmt.setString(2, pw);

			// SELECT文を実行
			ResultSet rs = pStmt.executeQuery();

			// 結果が存在すればユーザーを確認
			while (rs.next()) {
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

	// ユーザーをデータベースに登録するメソッド
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

			// 一度データベース内の情報を一括検索し、すでに存在していないか確認するSQL
			String sql_deleted_check = "SELECT * FROM USERS WHERE USER_NAME = ? AND USER_PASS = ?";
			PreparedStatement pStmt1 = conn.prepareStatement(sql_deleted_check);

			pStmt1.setString(1, nm);
			pStmt1.setString(2, pw);

			ResultSet rs = pStmt1.executeQuery();

			if (rs.next() && rs.getInt("IS_DELETED") == 1) {
				// 入力したユーザ名とパスワードが存在していて、かつ削除済みの場合
				// UPDATE文の準備（指定したユーザー名またはパスワードでユーザーを削除）
				String sql_restore_user = "UPDATE USERS SET IS_DELETED = 0 WHERE USER_NAME=? AND USER_PASS=?";
				PreparedStatement pStmt = conn.prepareStatement(sql_restore_user);

				pStmt.setString(1, nm);
				pStmt.setString(2, pw);
				
				// UPDATE文を実行
				int delete = pStmt.executeUpdate();
				if (delete > 0) {
					System.out.println("削除済みのユーザーを復元しました。");
					return true;
				} else {
					System.out.println("削除に失敗しました。");
					return false;
				}
				
			} else if (rs.next() && rs.getInt("IS_DELETED") == 0) {
				// 入力したユーザ名とパスワードが存在していて、削除されていない場合
				System.out.println("このユーザーはすでに存在しています。");
				return false;
				
			} else {
				// データベースに入力したユーザ名とパスワードが存在しない場合（新規登録）
				// INSERT文の準備（新規ユーザーをデータベースに登録）
				String sql = "INSERT INTO USERS(USER_NAME, USER_PASS) VALUES (?, ?)";
				PreparedStatement pStmt = conn.prepareStatement(sql);

				pStmt.setString(1, nm);
				pStmt.setString(2, pw);

				// INSERT文を実行
				int result = pStmt.executeUpdate();

				// 登録成功か確認
				if (result > 0) {
					System.out.println("ユーザー情報を正常に登録しました。");
					return true;
				} else {
					System.out.println("登録に失敗しました。");
					return false;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace(); // SQLエラーを表示
			System.out.println("Error : UserDAO.registerUser");
			return false; // 失敗
		}
	}

	// データベースからユーザーを見た目上削除するメソッド
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
