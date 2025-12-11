package service;

import java.util.List;

import dao.MusicDAO;
import model.Music;

/**
 * 検索結果に関するビジネスロジックを提供するサービスクラス
 */
public class SearchResultService {
	private MusicDAO dao = new MusicDAO();
	
	/**
	 * 検索結果を取得
	 * @param music 検索条件を含むMusicオブジェクト
	 * @return 検索結果の音楽リスト
	 */
	public List<Music> execute(Music music) {
		String searchWord = music.getSearchText();
		return dao.searchMusic(searchWord);
	}
}
