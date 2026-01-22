package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Music;
import util.DatabaseConnection;

/**
 * 音楽情報を管理するDAOクラス
 */
public class MusicDAO {

	/**
	 * トップページ用のランダムな曲リストを取得
	 * @return 音楽リスト
	 */
	public List<Music> topFindAll() {
		List<Music> list = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection()) {
			String sql = "SELECT * FROM MUSICS ORDER BY RAND()";
			try (PreparedStatement ps = conn.prepareStatement(sql);
					ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(new Music(
							rs.getInt("ID"),
							rs.getString("TITLE"),
							rs.getString("ARTIST"),
							rs.getInt("MUSIC_TIME"),
							rs.getInt("LIKES"),
							rs.getString("URL")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 検索ワードで音楽を検索
	 * @param searchWord 検索ワード
	 * @return 検索結果の音楽リスト
	 */
	public List<Music> searchMusic(String searchWord) {
		List<Music> musicList = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection()) {
			String sql = "SELECT ID,TITLE,ARTIST,LIKES,URL FROM MUSICS WHERE TITLE LIKE ? OR ARTIST LIKE ?";
			try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
				String searchPattern = "%" + searchWord + "%";
				pStmt.setString(1, searchPattern);
				pStmt.setString(2, searchPattern);

				try (ResultSet rs = pStmt.executeQuery()) {
					while (rs.next()) {
						musicList.add(new Music(
								rs.getInt("ID"),
								rs.getString("TITLE"),
								rs.getString("ARTIST"),
								rs.getInt("LIKES"),
								rs.getString("URL")));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return musicList;
	}

	/**
	 * いいね数順のランキングを取得
	 * @return ランキングの音楽リスト
	 */
	public List<Music> getRanking() {
		List<Music> musicList = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection()) {
			// LIKES が NULL の場合に備えて 0 扱いにし、URL も取得してランキング画面のリンク生成で落ちないようにする
			String sql = "SELECT ID, TITLE, ARTIST, IFNULL(LIKES, 0) AS LIKES, URL " +
					"FROM MUSICS " +
					"ORDER BY IFNULL(LIKES, 0) DESC, ID ASC";
			try (PreparedStatement pStmt = conn.prepareStatement(sql);
					ResultSet rs = pStmt.executeQuery()) {
				while (rs.next()) {
					musicList.add(new Music(
							rs.getInt("ID"),
							rs.getString("TITLE"),
							rs.getString("ARTIST"),
							rs.getInt("LIKES"),
							rs.getString("URL")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return musicList;
	}

	/**
	 * 音楽をデータベースに登録
	 * @param title タイトル
	 * @param genre ジャンル
	 * @param artist アーティスト
	 * @param lyricist 作詞家
	 * @param composer 作曲家
	 * @param releaseYmd 発売年月日
	 * @param musicTime 再生時間
	 * @param url URL
	 */
	public void insert(String title, String genre, String artist, String lyricist, String composer, int releaseYmd,
			int musicTime, String url) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			String sql = "INSERT INTO musics(title, genre, artist, lyricist, composer, release_ymd, music_time, url) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
				pStmt.setString(1, title);
				pStmt.setString(2, genre);
				pStmt.setString(3, artist);
				pStmt.setString(4, lyricist);
				pStmt.setString(5, composer);
				pStmt.setInt(6, releaseYmd);
				pStmt.setInt(7, musicTime);
				pStmt.setString(8, url);
				pStmt.executeUpdate();
				System.out.println(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 音楽にいいねを追加
	 * @param id 音楽ID
	 * @return 更新成功時はtrue、失敗時はfalse
	 */
	public boolean likeMusic(int id) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			// LIKES が NULL の場合、"NULL+1" は NULL のままになるため 0 扱いして加算する
			String sql = "UPDATE MUSICS SET LIKES = IFNULL(LIKES, 0) + 1 WHERE ID=?";
			try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
				pStmt.setInt(1, id);
				return pStmt.executeUpdate() > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * IDで音楽を取得
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
	 * URLで音楽を取得
	 * @param url 音楽URL
	 * @return 音楽オブジェクト、見つからない場合はnull
	 */
	public Music findByUrl(String url) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			String sql = "SELECT ID, TITLE, ARTIST, MUSIC_TIME, LIKES, URL FROM MUSICS WHERE url = ?";
			try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
				pStmt.setString(1, url);
				try (ResultSet rs = pStmt.executeQuery()) {
					if (rs.next()) {
						return new Music(
								rs.getInt("ID"),
								rs.getString("TITLE"),
								rs.getString("ARTIST"),
								rs.getInt("MUSIC_TIME"),
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
	 * ID順で曲リストを取得（次の曲ボタン用）
	 * @return ID順の音楽リスト
	 */
	public List<Music> getMusicListByIdOrder() {
		List<Music> list = new ArrayList<>();
		try (Connection conn = DatabaseConnection.getConnection()) {
			String sql = "SELECT * FROM MUSICS ORDER BY ID ASC";
			try (PreparedStatement ps = conn.prepareStatement(sql);
					ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(new Music(
							rs.getInt("ID"),
							rs.getString("TITLE"),
							rs.getString("ARTIST"),
							rs.getInt("MUSIC_TIME"),
							rs.getInt("LIKES"),
							rs.getString("URL")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 現在のIDから次のIDの曲を取得（ID順、循環）
	 * @param currentId 現在の音楽ID
	 * @return 次の音楽オブジェクト、見つからない場合は最初の曲
	 */
	public Music getNextMusicById(int currentId) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			// 現在のIDより大きい最小のIDを取得（次の曲）
			String sql = "SELECT ID, TITLE, ARTIST, MUSIC_TIME, LIKES, URL FROM MUSICS WHERE ID > ? ORDER BY ID ASC LIMIT 1";
			try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
				pStmt.setInt(1, currentId);
				try (ResultSet rs = pStmt.executeQuery()) {
					if (rs.next()) {
						return new Music(
								rs.getInt("ID"),
								rs.getString("TITLE"),
								rs.getString("ARTIST"),
								rs.getInt("MUSIC_TIME"),
								rs.getInt("LIKES"),
								rs.getString("URL"));
					}
				}
			}

			// 次の曲がない場合は最初の曲を取得（循環）
			String sqlFirst = "SELECT ID, TITLE, ARTIST, MUSIC_TIME, LIKES, URL FROM MUSICS ORDER BY ID ASC LIMIT 1";
			try (PreparedStatement pStmtFirst = conn.prepareStatement(sqlFirst);
					ResultSet rsFirst = pStmtFirst.executeQuery()) {
				if (rsFirst.next()) {
					return new Music(
							rsFirst.getInt("ID"),
							rsFirst.getString("TITLE"),
							rsFirst.getString("ARTIST"),
							rsFirst.getInt("MUSIC_TIME"),
							rsFirst.getInt("LIKES"),
							rsFirst.getString("URL"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 現在のIDから前のIDの曲を取得（ID順、循環）
	 * @param currentId 現在の音楽ID
	 * @return 前の音楽オブジェクト、見つからない場合は最後の曲
	 */
	public Music getPrevMusicById(int currentId) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			// 現在のIDより小さい最大のIDを取得（前の曲）
			String sql = "SELECT ID, TITLE, ARTIST, MUSIC_TIME, LIKES, URL FROM MUSICS WHERE ID < ? ORDER BY ID DESC LIMIT 1";
			try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
				pStmt.setInt(1, currentId);
				try (ResultSet rs = pStmt.executeQuery()) {
					if (rs.next()) {
						return new Music(
								rs.getInt("ID"),
								rs.getString("TITLE"),
								rs.getString("ARTIST"),
								rs.getInt("MUSIC_TIME"),
								rs.getInt("LIKES"),
								rs.getString("URL"));
					}
				}
			}

			// 前の曲がない場合は最後の曲を取得（循環）
			String sqlLast = "SELECT ID, TITLE, ARTIST, MUSIC_TIME, LIKES, URL FROM MUSICS ORDER BY ID DESC LIMIT 1";
			try (PreparedStatement pStmtLast = conn.prepareStatement(sqlLast);
					ResultSet rsLast = pStmtLast.executeQuery()) {
				if (rsLast.next()) {
					return new Music(
							rsLast.getInt("ID"),
							rsLast.getString("TITLE"),
							rsLast.getString("ARTIST"),
							rsLast.getInt("MUSIC_TIME"),
							rsLast.getInt("LIKES"),
							rsLast.getString("URL"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
