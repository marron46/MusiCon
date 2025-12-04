package model.logic;

import dao.BookmarkDAO;
import model.Music;
import model.User;

public class MyBookmarkLogic {
	private BookmarkDAO dao = new BookmarkDAO();
	
	public boolean execute(User user, Music music) {
		return dao.registerBookmark(user, music);
	}
}
