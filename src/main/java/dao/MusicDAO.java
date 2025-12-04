package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Music;

public class MusicDAO {

	private final String JDBC_URL = "jdbc:mysql://localhost/musicon";
	private final String DB_USER = "root";
	private final String DB_PASS = "";

	public List<Music> topFindAll() {// 書き足し
		List<Music> list = new ArrayList<>();
		// JDBCドライバを読み込む
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
			String sql = "SELECT ID, TITLE, LIKES, URL FROM MUSICS";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Music(
						rs.getInt("ID"),
						rs.getString("TITLE"),
						rs.getInt("LIKES"),
						rs.getString("URL")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Music> searchMusic(String str_searchWord) {
		List<Music> musicList = new ArrayList<>();
		// JDBCドライバを読み込む
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}

		// データベース接続
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {

			// SELECT文の準備（データベースからmusicsを取得）
			String sql = "SELECT ID,TITLE,ARTIST,LIKES,URL FROM MUSICS WHERE TITLE LIKE ? OR ARTIST LIKE ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, "%" + str_searchWord + "%");
			pStmt.setString(2, "%" + str_searchWord + "%");

			// SELECT文を実行
			ResultSet rs = pStmt.executeQuery();

			// 結果をArrayListに格納
			while (rs.next()) {
				int int_id = rs.getInt("ID");
				String str_title = rs.getString("TITLE");
				String str_artist = rs.getString("ARTIST");
				int int_likes = rs.getInt("LIKES");
				String str_url = rs.getString("URL");

				// 新しいMutterオブジェクトを作成
				Music music = new Music(int_id, str_title, str_artist, int_likes, str_url);
				musicList.add(music); // リストに追加
			}
		} catch (SQLException e) {
			e.printStackTrace(); // SQLエラーを表示
			return null;
		}
		return musicList; // 全てのmusicListリストを返す
	}

	public List<Music> getRanking() {
		List<Music> musicList = new ArrayList<>();
		// JDBCドライバを読み込む
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}

		// データベース接続
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {

			// SELECT文の準備（データベースからmusicsを取得）
			String sql = "SELECT ID, TITLE, ARTIST, LIKES FROM MUSICS ORDER BY LIKES DESC, ID ASC";
			PreparedStatement pStmt = conn.prepareStatement(sql);

			// SELECT文を実行
			ResultSet rs = pStmt.executeQuery();

			// 結果をArrayListに格納
			while (rs.next()) {
				int int_id = rs.getInt("ID");
				String str_title = rs.getString("TITLE");
				String str_artist = rs.getString("ARTIST");
				int int_likes = rs.getInt("LIKES");

				// 新しいMutterオブジェクトを作成
				Music music = new Music(int_id, str_title, str_artist, int_likes);
				musicList.add(music); // リストに追加
			}
		} catch (SQLException e) {
			e.printStackTrace(); // SQLエラーを表示
			return null;
		}
		return musicList; // 全てのMutterリストを返す
	}

	public void insert(String title, String genre, String artist, String lyricist, String composer, int release_ymd,
			int music_time, String url) {// 書き足し
		// JDBCドライバを読み込む
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		// データベース接続
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
			String sql = "INSERT INTO musics(title, genre, artist, lyricist, composer, release_ymd, music_time, url) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, title);
			pStmt.setString(2, genre);
			pStmt.setString(3, artist);
			pStmt.setString(4, lyricist);
			pStmt.setString(5, composer);
			pStmt.setInt(6, release_ymd);
			pStmt.setInt(7, music_time);
			pStmt.setString(8, url);

			pStmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean likeMusic(int id) {
		// JDBCドライバを読み込む
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}

		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {

			// INSERT文の準備（新規ユーザーをデータベースに登録）
			String sql = "UPDATE MUSICS SET LIKES = LIKES+1 WHERE ID=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);

			pStmt.setInt(1, id);
			
			// INSERT文を実行
			int result = pStmt.executeUpdate();

			// 登録成功か確認
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace(); // SQLエラーを表示
			System.out.println("Error : UserDAO.registerUser");
			return false; // 失敗lll
		}
	}

	// 曲再生のためのSQLです。自分の書き方に合わせて書き換えてね( ´∀｀)b
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
}
