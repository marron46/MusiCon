package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Bookmark;
import model.Music;
import model.User;

public class BookmarkDAO {

	//データベース接続に使用する情報
	private final String JDBC_URL = "jdbc:mysql://localhost/musicon"; // JDBC接続URL
	private final String DB_USER = "root"; // データベースユーザー名
	private final String DB_PASS = ""; // データベースパスワード

	public List<Bookmark> getBookmark(User user) {
		List<Bookmark> bookmarkList = new ArrayList<>();
		// JDBCドライバを読み込む
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}

		// データベース接続
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {

			// Usersテーブルで該当のユーザIDを取得
			String sql_get_userid = "SELECT USER_ID FROM USERS WHERE USER_NAME=?";
			PreparedStatement pStmt1 = conn.prepareStatement(sql_get_userid);
			pStmt1.setString(1, user.getUserName());
			ResultSet rs1 = pStmt1.executeQuery();
			int int_userid = rs1.getInt("USER_ID");

			// SELECT文の準備（データベースからbookmarksを取得）
			// 取得したユーザIDと一致しているBookmarksテーブルのレコードを全て取得
			String sql_bookmark = "SELECT * FROM BOOKMARKS WHERE B_USER=? ORDER BY BOOKMARK_ID";
			PreparedStatement pStmt2 = conn.prepareStatement(sql_bookmark);

			pStmt2.setInt(1, int_userid);

			// SELECT文を実行
			ResultSet rs2 = pStmt2.executeQuery();

			PreparedStatement pStmt3;
			// 結果をArrayListに格納
			while (rs2.next()) {
				int int_bookmark_id = rs2.getInt("BOOKMARK_ID");
				int int_Bmusic = rs2.getInt("B_MUSIC");

				// BookmarkテーブルとMusicテーブルの情報を照合させる
				String sql_bookmark_to_music = "SELECT TITLE,ARTIST FROM MUSIC WHERE ID=?";
				pStmt3 = conn.prepareStatement(sql_bookmark_to_music);

				pStmt3.setInt(1, int_Bmusic);
				ResultSet rs3 = pStmt2.executeQuery();

				String str_title = rs3.getString("TITLE");
				String str_artist = rs3.getString("ARTIST");

				// 新しいオブジェクトを作成
				Bookmark bookmark = new Bookmark(int_bookmark_id, str_title, str_artist);
				bookmarkList.add(bookmark); // リストに追加
			}
		} catch (SQLException e) {
			e.printStackTrace(); // SQLエラーを表示
			System.out.println("Error : BookmarkDAO.getBookmark");
			return null;
		}
		return bookmarkList; // 全てのbookmarkListを返す
	}

	public boolean registerBookmark(User user, Music music) {
		// JDBCドライバを読み込む
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}

		// データベース接続
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {

			// Usersテーブルで該当のユーザIDを取得
			String sql_get_userid = "SELECT USER_ID FROM USERS WHERE USER_NAME=?";
			PreparedStatement pStmt1 = conn.prepareStatement(sql_get_userid);
			pStmt1.setString(1, user.getUserName());
			ResultSet rs1 = pStmt1.executeQuery();
			int int_userid = rs1.getInt("USER_ID");

			// Musicテーブルで音楽名から音楽IDを取得
			String sql_get_musicid = "SELECT MUSIC_ID FROM USERS WHERE TITLE=?";
			PreparedStatement pStmt2 = conn.prepareStatement(sql_get_musicid);
			pStmt2.setString(1, music.getTitle());
			ResultSet rs2 = pStmt2.executeQuery();
			int int_musicid = rs2.getInt("MUSIC_ID");

			// INSERT文の準備（新規ユーザーをデータベースに登録）
			String sql = "INSERT INTO BOOKMARKS(B_USER, B_MUSIC) VALUES (?, ?)";
			PreparedStatement pStmt = conn.prepareStatement(sql);

			pStmt.setInt(1, int_userid);
			pStmt.setInt(2, int_musicid);
			
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
}
