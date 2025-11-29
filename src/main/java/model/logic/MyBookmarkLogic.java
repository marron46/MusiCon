package model.logic;

import java.util.List;

import dao.BookmarkDAO;
import model.Bookmark;
import model.User;

public class MyBookmarkLogic {
	private BookmarkDAO dao = new BookmarkDAO();
	
	//マイページ情報の取得
	public List<Bookmark> getBookmark(User user) {
		List<Bookmark> allList = dao.getBookmark(user);
		return allList;
	}
}
