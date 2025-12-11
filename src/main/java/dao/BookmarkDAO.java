package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Bookmark;
import model.Music;
import model.User;
import util.DatabaseConnection;

/**
 * ブックマーク情報を管理するDAOクラス
 */
public class BookmarkDAO {

	/**
	 * ユーザーのブックマーク一覧を取得（INDEX順）
	 * @param user ユーザー情報
	 * @return ブックマークリスト
	 */
	public List<Bookmark> getBookmark(User user) {
		List<Bookmark> bookmarkList = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection()) {

			// ユーザーIDを取得
			int userId = getUserId(conn, user.getUserName());
			if (userId == 0) {
				return bookmarkList;
			}
			
			// INDEX順でブックマークを取得
			String sql = "SELECT * FROM BOOKMARKS WHERE B_USER=? ORDER BY COALESCE(B_INDEX, BOOKMARK_ID) ASC";
			try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
				pStmt.setInt(1, userId);
				try (ResultSet rs = pStmt.executeQuery()) {
					int currentIndex = 0;
					while (rs.next()) {
						int bookmarkId = rs.getInt("BOOKMARK_ID");
						int musicId = rs.getInt("B_MUSIC");
						int bookmarkIndex = rs.getInt("B_INDEX");
						if (rs.wasNull()) {
							bookmarkIndex = currentIndex;
						}

						// Music情報を取得
						Music music = getMusicById(conn, musicId);
						if (music != null) {
							bookmarkList.add(new Bookmark(
									bookmarkId,
									music.getTitle(),
									music.getArtist(),
									musicId,
									bookmarkIndex,
									music.getUrl()));
							currentIndex++;
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return bookmarkList;
	}
	
	/**
	 * ユーザー名からユーザーIDを取得（内部メソッド）
	 */
	private int getUserId(Connection conn, String userName) throws SQLException {
		String sql = "SELECT USER_ID FROM USERS WHERE USER_NAME=?";
		try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
			pStmt.setString(1, userName);
			try (ResultSet rs = pStmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("USER_ID");
				}
			}
		}
		return 0;
	}
	
	/**
	 * 音楽IDからMusicオブジェクトを取得（内部メソッド）
	 */
	private Music getMusicById(Connection conn, int musicId) throws SQLException {
		String sql = "SELECT TITLE, ARTIST, URL FROM MUSICS WHERE ID=?";
		try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
			pStmt.setInt(1, musicId);
			try (ResultSet rs = pStmt.executeQuery()) {
				if (rs.next()) {
					// Musicオブジェクトを作成するために、一時的なMusicオブジェクトを作成
					// 実際のMusicオブジェクトはURLから取得する必要がある
					return new Music(0, rs.getString("TITLE"), rs.getString("ARTIST"), 0, 0, rs.getString("URL"));
				}
			}
		}
		return null;
	}

	/**
	 * ブックマークを登録
	 * @param user ユーザー情報
	 * @param music 音楽情報
	 * @return 登録成功時はtrue、失敗時はfalse（既に登録済みの場合はfalse）
	 */
	public boolean registerBookmark(User user, Music music) {
		try (Connection conn = DatabaseConnection.getConnection()) {

			int userId = getUserId(conn, user.getUserName());
			if (userId == 0) {
				return false;
			}

			// Musicテーブルで音楽名から音楽IDを取得
			int musicId = getMusicIdByTitle(conn, music.getTitle());
			if (musicId == 0) {
				return false;
			}

			// 既にブックマーク済みかチェック
			String checkSql = "SELECT B_USER, B_MUSIC FROM BOOKMARKS WHERE B_USER = ? AND B_MUSIC = ?";
			try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
				checkStmt.setInt(1, userId);
				checkStmt.setInt(2, musicId);
				try (ResultSet rs = checkStmt.executeQuery()) {
					if (rs.next()) {
						// 既に登録済み
						return false;
					}
				}
			}

			// 現在のユーザーのブックマーク数を取得してINDEXを決定
			int nextIndex = getBookmarkCount(conn, userId);

			// ブックマークを登録
			String sql = "INSERT INTO BOOKMARKS(B_USER, B_MUSIC, B_INDEX) VALUES (?, ?, ?)";
			try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
				pStmt.setInt(1, userId);
				pStmt.setInt(2, musicId);
				pStmt.setInt(3, nextIndex);
				return pStmt.executeUpdate() > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * タイトルから音楽IDを取得（内部メソッド）
	 */
	private int getMusicIdByTitle(Connection conn, String title) throws SQLException {
		String sql = "SELECT ID FROM MUSICS WHERE TITLE=?";
		try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
			pStmt.setString(1, title);
			try (ResultSet rs = pStmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("ID");
				}
			}
		}
		return 0;
	}
	
	/**
	 * ユーザーのブックマーク数を取得（内部メソッド）
	 */
	private int getBookmarkCount(Connection conn, int userId) throws SQLException {
		String sql = "SELECT COUNT(*) FROM BOOKMARKS WHERE B_USER=?";
		try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
			pStmt.setInt(1, userId);
			try (ResultSet rs = pStmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}
		return 0;
	}
	
	/**
	 * 曲IDから曲を取得
	 * @param id 音楽ID
	 * @return 音楽オブジェクト、見つからない場合はnull
	 */
	public Music playMusicById(int id) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			String sql = "SELECT ID, TITLE, ARTIST, LIKES, URL FROM MUSICS WHERE ID = ?";
			try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
				pStmt.setInt(1, id);
				try (ResultSet rs = pStmt.executeQuery()) {
					if (rs.next()) {
						return new Music(
								rs.getInt("ID"),
								rs.getString("TITLE"),
								rs.getString("ARTIST"),
								rs.getInt("LIKES"),
								rs.getString("URL"));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ブックマークに存在するかチェック
	 * @param user ユーザー情報
	 * @param music 音楽情報
	 * @return 存在する場合はtrue、存在しない場合はfalse
	 */
	public boolean isBookmarked(User user, Music music) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			int userId = getUserId(conn, user.getUserName());
			if (userId == 0) {
				return false;
			}
			
			String sql = "SELECT B_USER, B_MUSIC FROM BOOKMARKS WHERE B_USER = ? AND B_MUSIC = ?";
			try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
				pStmt.setInt(1, userId);
				pStmt.setInt(2, music.getId());
				try (ResultSet rs = pStmt.executeQuery()) {
					return rs.next();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * URLベースでブックマークを取得
	 * @param user ユーザー情報
	 * @param musicUrl 音楽URL
	 * @return ブックマークリスト
	 */
	public List<Bookmark> getBookmarkByUrl(User user, String musicUrl) {
		List<Bookmark> bookmarkList = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection()) {
			int userId = getUserId(conn, user.getUserName());
			if (userId == 0) {
				return bookmarkList;
			}
			
			// URLからMusic IDを取得
			int musicId = getMusicIdByUrl(conn, musicUrl);
			if (musicId == 0) {
				return bookmarkList;
			}
			
			// ブックマークを取得
			String sql = "SELECT * FROM BOOKMARKS WHERE B_USER=? AND B_MUSIC=? ORDER BY COALESCE(B_INDEX, BOOKMARK_ID) ASC";
			try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
				pStmt.setInt(1, userId);
				pStmt.setInt(2, musicId);
				try (ResultSet rs = pStmt.executeQuery()) {
					int currentIndex = 0;
					while (rs.next()) {
						int bookmarkId = rs.getInt("BOOKMARK_ID");
						int bookmarkIndex = rs.getInt("B_INDEX");
						if (rs.wasNull()) {
							bookmarkIndex = currentIndex;
						}

						Music music = getMusicById(conn, musicId);
						if (music != null) {
							bookmarkList.add(new Bookmark(
									bookmarkId,
									music.getTitle(),
									music.getArtist(),
									musicId,
									bookmarkIndex,
									music.getUrl()));
							currentIndex++;
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return bookmarkList;
	}
	
	/**
	 * URLから音楽IDを取得（内部メソッド）
	 */
	private int getMusicIdByUrl(Connection conn, String url) throws SQLException {
		String sql = "SELECT ID FROM MUSICS WHERE URL=?";
		try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
			pStmt.setString(1, url);
			try (ResultSet rs = pStmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("ID");
				}
			}
		}
		return 0;
	}
	
	/**
	 * INDEX順でブックマークリストを取得（再生用）
	 * @param user ユーザー情報
	 * @return INDEX順のブックマークリスト
	 */
	public List<Bookmark> getBookmarkListOrderedByIndex(User user) {
		return getBookmark(user);
	}
	
	/**
	 * ブックマークを削除
	 * @param user ユーザー情報
	 * @param music 音楽情報
	 * @return 削除成功時はtrue、失敗時はfalse
	 */
	public boolean deleteBookmark(User user, Music music) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			// ユーザーIDを取得
			int userId = getUserId(conn, user.getUserName());
			if (userId == 0) {
				return false;
			}
			
			String sql = "DELETE FROM BOOKMARKS WHERE B_USER = ? AND B_MUSIC = ?";
			try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
				pStmt.setInt(1, userId);
				pStmt.setInt(2, music.getId());
				return pStmt.executeUpdate() > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
}
