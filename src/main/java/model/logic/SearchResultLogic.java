package model.logic;

import java.util.List;

import dao.MusicDAO;
import model.Music;

public class SearchResultLogic {
	private MusicDAO dao = new MusicDAO();
	
	//検索結果を表示する
	public List<Music> getsearchMusic(Music music) {
		
		// Musicから検索を取得
		String str_searchWord = music.getSearchText();

		return dao.searchMusic(str_searchWord);
		}
}