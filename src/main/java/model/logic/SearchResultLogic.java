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
		
		dao.searchMusic(str_searchWord);
		
		return allList;
		}
}

/*public List<Music> getRanking() {
	// DAO でランキング全件取得
	List<Music> allList = dao.getRanking();

	// --- ここで TOP10 に制限 ---
	if (allList.size() > 10) {
		return allList.subList(0, 10); // リストの10件目までを返す
	}
	return allList;
}*/
