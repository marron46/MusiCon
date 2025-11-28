package model.logic;

import java.util.List;

import model.Music;

public class MyBookmarkLogic {
	private BookmarkDAO dao = new BookmarkDAO();
	
	//マイページ情報の取得
	public List<Music> getBookmark(User user,Music music) {
		List<Music> allList = dao.getBookmark();
	}
}
