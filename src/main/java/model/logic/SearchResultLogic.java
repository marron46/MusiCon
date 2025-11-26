package model.logic;

import dao.MusicDAO;

public class SearchResultLogic {
	
	//検索結果を表示する
	public boolean execute(Music music) {
		MusicDAO dao = new MusicDAO();
		if(dao./*メソッド名*/()) {
			return true;
		}
		return false;
	}
}
