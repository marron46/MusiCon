package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.BookmarkDAO;
import dao.MusicDAO;
import model.Bookmark;
import model.Music;
import model.User;
import model.logic.MyBookmarkLogic;

@WebServlet("/BookmarkPlay")
public class BookmarkPlay extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();

		// ブックマークリストを取得
		List<Bookmark> bookmarkList = (List<Bookmark>) session.getAttribute("bookmarkList");
		if (bookmarkList == null || bookmarkList.isEmpty()) {
			request.setAttribute("message", "ブックマークがありません");
			RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/myBookmark.jsp");
			dispatcher.forward(request, response);
			return;
		}

		// index を取得（最初のアクセス時は id から検索して index を特定）
		String indexStr = request.getParameter("index");
		String idStr = request.getParameter("id");

		// ブックマークに入っている Music をリスト化（Music のみ）
		List<Music> musicList = new ArrayList<>();
		MusicDAO dao = new MusicDAO();

		for (Bookmark b : bookmarkList) {
			Music m = dao.playMusicById(b.getMusic_id());
			if (m != null) {
				musicList.add(m);
			}
		}

		if (musicList.isEmpty()) {
			request.setAttribute("message", "ブックマークに曲が存在しません");
			RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/myBookmark.jsp");
			dispatcher.forward(request, response);
			return;
		}

		int index = 0;

		// index が指定されている場合（前へ/次へ/自動再生）
		if (indexStr != null) {
			index = Integer.parseInt(indexStr);
		}
		// 最初のアクセス時（id から index を自動算出）
		else if (idStr != null) {
			int musicId = Integer.parseInt(idStr);

			for (int i = 0; i < musicList.size(); i++) {
				if (musicList.get(i).getId() == musicId) {
					index = i;
					break;
				}
			}
		}

		// index の範囲を循環させる
		index = (index + musicList.size()) % musicList.size();

		// 現在の曲
		Music music = musicList.get(index);
		System.out.println("BookmarkPlayGet music:" + music);
		User user = (User) session.getAttribute("loginUser");

		// ★ すでにブックマーク済みかどうか
		boolean isBookmarked = new BookmarkDAO().isBookmarked(user, music);
		request.setAttribute("isBookmarked", isBookmarked);

		// JSP に渡す
		request.setAttribute("music", music);
		System.out.println("BookmarkPlay(スコープ確認) music:" + music);
		request.setAttribute("musicList", musicList);
		request.setAttribute("index", index);
		System.out.println("BookmarkPlayGet index:" + index);

		BookmarkDAO b_dao = new BookmarkDAO();
		request.setAttribute("isBookmarked", b_dao.isBookmarked(user, music));

		// フォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/playBookmark.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {//直近のセッションスコープ（music）には

		request.setCharacterEncoding("UTF-8");
		
		MusicDAO mdao = new MusicDAO();

		// int musicId = Integer.parseInt(request.getParameter("id"));
		int index = Integer.parseInt(request.getParameter("index"));
		System.out.println("BookmarkPlayPost index:" + index);
		
		// JSP で渡した "id" を取得
	    String idStr = request.getParameter("id");
	    int id = Integer.parseInt(idStr);
	    System.out.println("受け取ったMusic ID = " + id);

		HttpSession session = request.getSession();
		
		User user = (User) session.getAttribute("loginUser");
		System.out.println("user情報(BookmarkPlay):" + user);
		
		Music music = mdao.playMusicById(id);
		System.out.println("BookmarkPlay music:" + music);

		MyBookmarkLogic logic = new MyBookmarkLogic();
		logic.toggleBookmark(user, music);

		// 元の曲に戻る
		response.sendRedirect("playBookmark.jsp");
	}
}
