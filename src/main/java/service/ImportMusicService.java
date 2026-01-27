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
	 * @param artist アーティスト
	 * @param releaseYMD 発売年月日
	 * @param musicTime 再生時間
	 * @param genre ジャンル
	 * @param url URL
	 */
	public void addMusic(String title, String genre, String artist, int releaseY,
			int musicTime, String url) {
		dao.insert(title, genre, artist, releaseY, musicTime, url);
	}
}