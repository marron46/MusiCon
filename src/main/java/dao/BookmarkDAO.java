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
			System.out.println("username:" + user.getUserName());
			ResultSet rs1 = pStmt1.executeQuery();
			int int_userid = 0;
			if(rs1.next()) {
				int_userid = rs1.getInt("USER_ID");
			}
			
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
				String sql_bookmark_to_music = "SELECT TITLE,ARTIST FROM MUSICS WHERE ID=?";
				pStmt3 = conn.prepareStatement(sql_bookmark_to_music);

				pStmt3.setInt(1, int_Bmusic);
				ResultSet rs3 = pStmt3.executeQuery();

				if (rs3.next()) {
					String str_title = rs3.getString("TITLE");
					String str_artist = rs3.getString("ARTIST");

					// 新しいオブジェクトを作成
					Bookmark bookmark = new Bookmark(int_bookmark_id, str_title, str_artist, int_Bmusic);
					bookmarkList.add(bookmark); // リストに追加
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // SQLエラーを表示
			System.out.println("Error : BookmarkDAO.getBookmark");
			return null;
		}
		return bookmarkList; // 全てのbookmarkListを返す
	}

	// ユーザ名からユーザID、音楽名から音楽IDを検索しブックマークとして保存（仮）
	public boolean registerBookmark(User user, Music music) {
		// JDBCドライバを読み込む
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}

		// データベース接続
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {

			//TODO UserとMusicがすでにテーブル内にある場合は弾く機能を作る

			// Usersテーブルで該当のユーザIDを取得
			String sql_get_userid = "SELECT USER_ID FROM USERS WHERE USER_NAME=?";
			PreparedStatement pStmt1 = conn.prepareStatement(sql_get_userid);
			pStmt1.setString(1, user.getUserName());
			ResultSet rs1 = pStmt1.executeQuery();
			if (!rs1.next())
				return false;
			int int_userid = rs1.getInt("USER_ID");

			// Musicテーブルで音楽名から音楽IDを取得
			String sql_get_musicid = "SELECT ID FROM MUSICS WHERE TITLE=?";
			PreparedStatement pStmt2 = conn.prepareStatement(sql_get_musicid);
			pStmt2.setString(1, music.getTitle());
			ResultSet rs2 = pStmt2.executeQuery();
			if (!rs2.next())
				return false;
			int int_musicid = rs2.getInt("ID");

			// ---- 重複チェック ----
			/* String sqlCheck = "SELECT COUNT(*) FROM BOOKMARKS WHERE B_USER=? AND B_MUSIC=?";
			PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck);
			stmtCheck.setInt(1, int_userid);
			stmtCheck.setInt(2, int_musicid);
			ResultSet rsCheck = stmtCheck.executeQuery();
			rsCheck.next();
			if (rsCheck.getInt(1) > 0)
				return false; // 既に登録済み→弾く		*/

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
	
	// 曲IDから曲を検索し返す
	public Music playMusicById(int id) {// 書き足し
		// JDBCドライバを読み込む
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}

		Music music = null;

		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {

			// SELECT文の準備（1曲のデータの取得）
			String sql = "SELECT ID, TITLE, ARTIST, LIKES, URL FROM MUSICS WHERE ID = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);

			// SELECT文の実行
			pStmt.setInt(1, id); // SQL の一つ目の ? に id をセット
			ResultSet rs = pStmt.executeQuery();

			if (rs.next()) {
				music = new Music(
						rs.getInt("ID"),
						rs.getString("TITLE"),
						rs.getString("ARTIST"),
						rs.getInt("LIKES"),
						rs.getString("URL"));
			}
			return music;

		} catch (SQLException e) {
			e.printStackTrace(); // SQLエラーを表示
			System.out.println("Error : UserDAO.registerUser");
			return null; // 失敗
		}
	}
	
	// すでにブックマークに存在しているかどうか調べて結果を返す（true = 存在する）
	public boolean isBookmarked(User user, Music music) {
		// JDBCドライバを読み込む
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)){
			
			String sql_existing_check = "SELECT * FROM BOOKMARKS WHERE B_USER = ? AND B_MUSIC = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql_existing_check);
			
			pStmt.setInt(1, user.getUserId());
			pStmt.setInt(2, music.getId());
			
			ResultSet rs = pStmt.executeQuery();
			System.out.println("rs:" + rs);
			
			return rs.next();
			
		} catch(SQLException e) {
			e.printStackTrace(); // SQLエラーを表示
			System.out.println("Error : BookmarkDAO.isBookmarked");
			return false; // 失敗
		}
	}
	
	// ブックマークテーブルから削除するメソッド
	public boolean deleteBookmark(User user, Music music) {
		// JDBCドライバを読み込む
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}

		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {

			// INSERT文の準備（新規ユーザーをデータベースに登録）
			String sql_delete_from_bookmarks = "DELETE FROM BOOKMARKS WHERE B_USER = ? AND B_MUSIC = ? ";
			PreparedStatement pStmt = conn.prepareStatement(sql_delete_from_bookmarks);

			pStmt.setInt(1, user.getUserId());
			pStmt.setInt(2, music.getId());
			
			// INSERT文を実行
			int result = pStmt.executeUpdate();

			// 登録成功か確認
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace(); // SQLエラーを表示
			System.out.println("Error : BookmarkDAO.deleteBookmark");
			return false; // 失敗
		}
	}
	
	
	
}
