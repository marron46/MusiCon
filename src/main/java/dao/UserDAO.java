package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;
import util.DatabaseConnection;

public class UserDAO {

	/* ログインを実行する
	 * @param userName ユーザー名
	 * @param passward パスワード
	 * @return ログイン成功時はUserオブジェクト、失敗時はnull
	 */
	public User executeLogin(String userName, String password) {

		try (Connection conn = DatabaseConnection.getConnection()) {

			String sql = "SELECT * FROM USERS WHERE USER_NAME=? AND USER_PASS=? AND IS_DELETED=0";
			try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
				pStmt.setString(1, userName);
				pStmt.setString(2, password);

				try (ResultSet rs = pStmt.executeQuery()) {
					if (rs.next()) {
						int userId = rs.getInt("USER_ID");
						String dbUserName = rs.getString("USER_NAME");
						String dbUserPass = rs.getString("USER_PASS");
						return new User(dbUserName, dbUserPass, userId);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/*
	 * ユーザーをデータベースに登録する
	 * @param user 登録するユーザー情報
	 * @return 登録成功時はtrue、失敗時はfalse
	 */
	public boolean registerUser(User user) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			String userName = user.getUserName();
			String password = user.getUserPass();

			// 既存ユーザーの確認
			String checkSql = "SELECT * FROM USERS WHERE USER_NAME = ? AND USER_PASS = ?";
			try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
				checkStmt.setString(1, userName);
				checkStmt.setString(2, password);

				try (ResultSet rs = checkStmt.executeQuery()) {
					if (rs.next()) {
						int isDeleted = rs.getInt("IS_DELETED");
						if (isDeleted == 1) {
							// 削除済みユーザーを復元
							String restoreSql = "UPDATE USERS SET IS_DELETED = 0 WHERE USER_NAME=? AND USER_PASS=?";
							try (PreparedStatement restoreStmt = conn.prepareStatement(restoreSql)) {
								restoreStmt.setString(1, userName);
								restoreStmt.setString(2, password);
								return restoreStmt.executeUpdate() > 0;
							}
						} else {
							// 既に存在するユーザー
							return false;
						}
					} else {
						// 新規登録
						String insertSql = "INSERT INTO USERS(USER_NAME, USER_PASS) VALUES (?, ?)";
						try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
							insertStmt.setString(1, userName);
							insertStmt.setString(2, password);
							return insertStmt.executeUpdate() > 0;
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * ユーザーを論理削除する
	 * @param user 削除するユーザー情報
	 * @return 削除成功時はtrue、失敗時はfalse
	 */
	public boolean deleteUser(User user, String user_pass) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			String sql = "UPDATE USERS SET IS_DELETED = 1 WHERE USER_NAME=? AND USER_PASS=?";
			try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
				pStmt.setString(1, user.getUserName());
				pStmt.setString(2, user_pass);
				return pStmt.executeUpdate() > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * ユーザー名からユーザーIDを取得する
	 * @param user ユーザー情報
	 * @return ユーザーIDを含むUserオブジェクト、見つからない場合はnull
	 */
	public User getUserId(User user) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			String sql = "SELECT USER_ID FROM USERS WHERE USER_NAME = ?";
			try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
				pStmt.setString(1, user.getUserName());

				try (ResultSet rs = pStmt.executeQuery()) {
					if (rs.next()) {
						return new User(rs.getInt("USER_ID"));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
