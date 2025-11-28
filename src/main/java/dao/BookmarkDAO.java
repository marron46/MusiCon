package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Bookmark;
import model.User;

public class BookmarkDAO {

	//データベース接続に使用する情報
		private final String JDBC_URL = "jdbc:mysql://localhost/dokotsudu"; // JDBC接続URL
		private final String DB_USER = "root"; // データベースユーザー名
		private final String DB_PASS = ""; // データベースパスワード
		
	public List<Bookmark> getBookmark(User user){
		List<Bookmark> bookmarkList = new ArrayList<>();
		// JDBCドライバを読み込む
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}

		// データベース接続
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {

			String sql_get_userid = "SELECT USER_ID FROM USERS WHERE USER_NAME=?";
			PreparedStatement pStmt1 = conn.prepareStatement(sql_get_userid);
			ResultSet rs1 = pStmt1.executeQuery();
			int int_userid = rs1.getInt("USER_ID");
			
			// SELECT文の準備（データベースからbookmarksを取得）
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
			return null;
		}
		return bookmarkList; // 全てのbookmarkListを返す
	}
}
