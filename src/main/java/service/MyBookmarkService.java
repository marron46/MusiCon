package service;

import java.util.ArrayList;
import java.util.List;

import dao.BookmarkDAO;
import model.Bookmark;
import model.Music;
import model.User;

/**
 * ブックマークに関するビジネスロジックを提供するサービスクラス
 */
public class MyBookmarkService {
	private BookmarkDAO dao = new BookmarkDAO();

	/**
	 * ブックマークを登録または削除
	 * @param user ユーザー情報
	 * @param music 音楽情報
	 * @return 成功時はtrue、失敗時はfalse
	 */
	public boolean execute(User user, Music music) {
		return dao.registerBookmark(user, music);
	}

	/**
	 * ブックマークの登録状態を切り替え
	 * @param user ユーザー情報
	 * @param music 音楽情報
	 * @return 成功時はtrue、失敗時はfalse
	 */
	public boolean toggleBookmark(User user, Music music) {
		if (dao.isBookmarked(user, music)) {
			return dao.deleteBookmark(user, music);
		} else {
			return dao.registerBookmark(user, music);
		}
	}

	/**
	 * ブックマーク一覧を取得
	 * @param user ユーザー情報
	 * @return ブックマークリスト
	 */
	public List<Bookmark> getBookmark(User user) {
		return dao.getBookmark(user);
	}

	/**
	 * ブックマーク一覧をMusicリスト形式で取得
	 * @param user ユーザー情報
	 * @return 音楽リスト
	 */
	public List<Music> getBookmarkMusicList(User user) {
		List<Bookmark> bookmarkList = dao.getBookmark(user);
		List<Music> musicList = new ArrayList<>();

		for (Bookmark b : bookmarkList) {
			musicList.add(b.getMusic());
		}

		return musicList;
	}
}
