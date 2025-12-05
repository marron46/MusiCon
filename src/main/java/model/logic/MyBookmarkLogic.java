package model.logic;

import java.util.ArrayList;
import java.util.List;

import dao.BookmarkDAO;
import model.Bookmark;
import model.Music;
import model.User;

public class MyBookmarkLogic {
    private BookmarkDAO dao = new BookmarkDAO();
    
    public boolean execute(User user, Music music) {
        return dao.registerBookmark(user, music);
    }
    
    // Bookmark データ取得
    public List<Bookmark> getBookmark(User user) {
        List<Bookmark> bookmarkList = dao.getBookmark(user);
        System.out.println("Logicでリスト取得:" + bookmarkList);
        return bookmarkList;
    }

    // ▼追加：Musicのリスト形式で返すバージョン
    public List<Music> getBookmarkMusicList(User user) {
        List<Bookmark> bookmarkList = dao.getBookmark(user);
        List<Music> musicList = new ArrayList<>();

        for (Bookmark b : bookmarkList) {
            musicList.add(b.getMusic());
        }

        System.out.println("Musicリスト変換: " + musicList);
        return musicList;
    }
}
