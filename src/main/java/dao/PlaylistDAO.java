package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Music;
import model.PlaylistItem;
import model.User;
import util.DatabaseConnection;

/**
 * プレイリスト（ユーザーごとの順序付きリスト）を管理するDAO
 *
 * 想定テーブル:
 *  - PLAYLIST_ITEMS(PLAYLIST_ITEM_ID PK, P_USER, P_MUSIC, P_POS)
 */
public class PlaylistDAO {

	public List<PlaylistItem> getPlaylist(User user) {
		List<PlaylistItem> items = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection()) {
			int userId = getUserId(conn, user.getUserName());
			if (userId == 0) return items;

			String sql =
					"SELECT " +
					"  pi.PLAYLIST_ITEM_ID, pi.P_USER, pi.P_MUSIC, pi.P_POS, " +
					"  m.ID, m.TITLE, m.ARTIST, IFNULL(m.LIKES, 0) AS LIKES, m.URL " +
					"FROM PLAYLIST_ITEMS pi " +
					"JOIN MUSICS m ON pi.P_MUSIC = m.ID " +
					"WHERE pi.P_USER = ? " +
					"ORDER BY pi.P_POS ASC, pi.PLAYLIST_ITEM_ID ASC";
			System.out.println(sql);
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, userId);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						Music music = new Music(
								rs.getInt("ID"),
								rs.getString("TITLE"),
								rs.getString("ARTIST"),
								rs.getInt("LIKES"),
								rs.getString("URL"));
						items.add(new PlaylistItem(
								rs.getInt("PLAYLIST_ITEM_ID"),
								rs.getInt("P_USER"),
								rs.getInt("P_MUSIC"),
								rs.getInt("P_POS"),
								music));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
			
		}
		return items;
	}

	public boolean isInPlaylist(User user, int musicId) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			int userId = getUserId(conn, user.getUserName());
			if (userId == 0) return false;

			String sql = "SELECT 1 FROM PLAYLIST_ITEMS WHERE P_USER = ? AND P_MUSIC = ? LIMIT 1";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, userId);
				ps.setInt(2, musicId);
				try (ResultSet rs = ps.executeQuery()) {
					return rs.next();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean addToPlaylist(User user, int musicId) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			int userId = getUserId(conn, user.getUserName());
			if (userId == 0) return false;

			// 既に存在するなら追加しない（重複禁止）
			if (exists(conn, userId, musicId)) return false;

			int nextPos = getNextPos(conn, userId);
			String sql = "INSERT INTO PLAYLIST_ITEMS(P_USER, P_MUSIC, P_POS) VALUES (?, ?, ?)";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, userId);
				ps.setInt(2, musicId);
				ps.setInt(3, nextPos);
				return ps.executeUpdate() > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean removeFromPlaylist(User user, int musicId) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			int userId = getUserId(conn, user.getUserName());
			if (userId == 0) return false;

			String sql = "DELETE FROM PLAYLIST_ITEMS WHERE P_USER = ? AND P_MUSIC = ?";
			boolean deleted;
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, userId);
				ps.setInt(2, musicId);
				deleted = ps.executeUpdate() > 0;
			}
			if (!deleted) return false;

			// 位置を詰める（0..n-1 に正規化）
			normalizePositions(conn, userId);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * playlist pos -> Music を取得（posは0-based）
	 */
	public Music getMusicByPos(User user, int pos) {
		List<PlaylistItem> items = getPlaylist(user);
		if (items == null || items.isEmpty()) return null;
		int safePos = (pos % items.size() + items.size()) % items.size();
		return items.get(safePos).getMusic();
	}

	public int getPlaylistSize(User user) {
		List<PlaylistItem> items = getPlaylist(user);
		return items == null ? 0 : items.size();
	}

	/**
	 * テーブル内の P_POS を 0..n-1 に詰め直す（順序は P_POS, PLAYLIST_ITEM_ID の昇順を維持）
	 */
	private void normalizePositions(Connection conn, int userId) throws SQLException {
		String selectSql = "SELECT PLAYLIST_ITEM_ID FROM PLAYLIST_ITEMS WHERE P_USER = ? ORDER BY P_POS ASC, PLAYLIST_ITEM_ID ASC";
		List<Integer> ids = new ArrayList<>();
		try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
			ps.setInt(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) ids.add(rs.getInt("PLAYLIST_ITEM_ID"));
			}
		}

		String updateSql = "UPDATE PLAYLIST_ITEMS SET P_POS = ? WHERE PLAYLIST_ITEM_ID = ?";
		try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
			for (int i = 0; i < ids.size(); i++) {
				ps.setInt(1, i);
				ps.setInt(2, ids.get(i));
				ps.addBatch();
			}
			ps.executeBatch();
		}
	}

	private boolean exists(Connection conn, int userId, int musicId) throws SQLException {
		String sql = "SELECT 1 FROM PLAYLIST_ITEMS WHERE P_USER = ? AND P_MUSIC = ? LIMIT 1";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, userId);
			ps.setInt(2, musicId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	private int getNextPos(Connection conn, int userId) throws SQLException {
		String sql = "SELECT IFNULL(MAX(P_POS), -1) + 1 AS NEXT_POS FROM PLAYLIST_ITEMS WHERE P_USER = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) return rs.getInt("NEXT_POS");
			}
		}
		return 0;
	}

	private int getUserId(Connection conn, String userName) throws SQLException {
		String sql = "SELECT USER_ID FROM USERS WHERE USER_NAME = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, userName);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) return rs.getInt("USER_ID");
			}
		}
		return 0;
	}
}


