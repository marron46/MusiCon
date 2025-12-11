package service;

import dao.MusicDAO;

/**
 * 音楽インポートに関するビジネスロジックを提供するサービスクラス
 */
public class ImportMusicService {
	private MusicDAO dao = new MusicDAO();

	/**
	 * 音楽を追加
	 * @param title タイトル
	 * @param genre ジャンル
	 * @param artist アーティスト
	 * @param lyricist 作詞家
	 * @param composer 作曲家
	 * @param releaseYMD 発売年月日
	 * @param musicTime 再生時間
	 * @param url URL
	 */
	public void addMusic(String title, String genre, String artist, String lyricist, String composer, int releaseYMD, int musicTime, String url) {
		dao.insert(title, genre, artist, lyricist, composer, releaseYMD, musicTime, url);
	}
}
