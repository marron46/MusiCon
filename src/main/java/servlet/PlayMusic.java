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
import service.PlayMusicService;

@WebServlet({"/PlayMusic", "/BookmarkPlay"})
public class PlayMusic extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		PlayMusicService service = new PlayMusicService();

		String idStr = request.getParameter("id");
		String urlStr = request.getParameter("url");
		String nextStr = request.getParameter("next");
		String prevStr = request.getParameter("prev");
		String bookmarkIndexStr = request.getParameter("bookmarkIndex");
		String indexStr = request.getParameter("index"); // BookmarkPlay互換性のため
		String bookmarkModeStr = request.getParameter("bookmarkMode");
		// BookmarkPlayからのアクセスかどうかを判定
		boolean isBookmarkPlayRequest = request.getServletPath().equals("/BookmarkPlay");
		System.out.println("idStr: " + idStr + ", urlStr: " + urlStr + ", bookmarkIndex: " + bookmarkIndexStr + ", index: " + indexStr);

		if (idStr == null && urlStr == null && nextStr == null && prevStr == null) {
			// ログインチェック
			HttpSession session = request.getSession();
			String userName = (String) session.getAttribute("user_name");
			System.out.println("userName:" + userName);
			if (userName == null) {
				response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
				return;
			}
			// 曲一覧を取得して JSP へ
			List<Music> musicList = service.getMusicList();
			// セッションスコープに保存
			session.setAttribute("musicList", musicList);
			System.out.println("DAOからとってきた曲リスト" + musicList);
			// フォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/top.jsp");
			dispatcher.forward(request, response);
			System.out.println("曲がないのでtopに戻ります");

		} else {
			// ログインチェック
			HttpSession session = request.getSession();
			String userName = (String) session.getAttribute("user_name");
			if (userName == null) {
				response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
				return;
			}
			
			Music music = null;
			// BookmarkPlayからのリクエスト、またはbookmarkMode/bookmarkIndex/indexパラメータがある場合はブックマークモード
			boolean isBookmarkMode = isBookmarkPlayRequest || "true".equals(bookmarkModeStr) || bookmarkIndexStr != null || indexStr != null;
			System.out.println("isBookmarkMode:" + isBookmarkMode);

			// ブックマーク再生モードの場合
			if (isBookmarkMode) {
				List<Bookmark> bookmarkList = (List<Bookmark>) session.getAttribute("bookmarkList");
				if (bookmarkList == null || bookmarkList.isEmpty()) {
					// BookmarkPlay互換性: エラーメッセージを設定してmyBookmark.jspにフォワード
					if (isBookmarkPlayRequest) {
						request.setAttribute("message", "ブックマークがありません");
						RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/myBookmark.jsp");
						dispatcher.forward(request, response);
						return;
					}
					response.sendRedirect(request.getContextPath() + "/MyBookmark");
					return;
				}

				// ブックマークに入っている Music をリスト化
				List<Music> musicList = new ArrayList<>();
				MusicDAO dao = new MusicDAO();

				for (Bookmark b : bookmarkList) {
					Music m = null;
					if (b.getMusic_url() != null && !b.getMusic_url().isEmpty()) {
						m = dao.findByUrl(b.getMusic_url());
					} else {
						m = dao.playMusicById(b.getMusic_id());
					}
					if (m != null) {
						musicList.add(m);
					}
				}
				System.out.println("PlayMusic時点でのbookmarkMusicList:" + musicList);

				if (musicList.isEmpty()) {
					// BookmarkPlay互換性: エラーメッセージを設定してmyBookmark.jspにフォワード
					if (isBookmarkPlayRequest) {
						request.setAttribute("message", "ブックマークに曲が存在しません");
						RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/myBookmark.jsp");
						dispatcher.forward(request, response);
						return;
					}
					response.sendRedirect(request.getContextPath() + "/MyBookmark");
					return;
				}

				int index = 0;

				// index が指定されている場合（BookmarkPlay互換性: indexパラメータを優先）
				if (indexStr != null && !indexStr.isEmpty()) {
					index = Integer.parseInt(indexStr);
				}
				// bookmarkIndex が指定されている場合（前へ/次へ/自動再生）
				else if (bookmarkIndexStr != null && !bookmarkIndexStr.isEmpty()) {
					index = Integer.parseInt(bookmarkIndexStr);
				}
				// 最初のアクセス時（url から index を自動算出）
				else if (urlStr != null && !urlStr.isEmpty()) {
					for (int i = 0; i < bookmarkList.size(); i++) {
						Bookmark b = bookmarkList.get(i);
						if (b.getMusic_url() != null && b.getMusic_url().equals(urlStr)) {
							index = i;
							break;
						}
					}
				}
				// next/prev パラメータがある場合
				else if (nextStr != null || prevStr != null) {
					// 現在のindexをセッションから取得
					Integer currentIndex = (Integer) session.getAttribute("currentBookmarkIndex");
					if (currentIndex == null) {
						currentIndex = 0;
					}

					if (nextStr != null) {
						index = (currentIndex + 1) % musicList.size();
					} else {
						index = (currentIndex - 1 + musicList.size()) % musicList.size();
					}
				}

				// index の範囲を循環させる
				index = (index + musicList.size()) % musicList.size();
				session.setAttribute("currentBookmarkIndex", index);

				// 現在の曲
				music = musicList.get(index);
				request.setAttribute("musicList", musicList);
				request.setAttribute("index", index);
				request.setAttribute("isBookmarkMode", true);
			}
			// 通常再生モードの場合
			else {
				// 次の曲ボタンが押された場合（ID順で次の曲を取得）
				if (nextStr != null && !nextStr.isEmpty()) {
					int currentId = Integer.parseInt(nextStr);
					music = service.getNextMusicById(currentId);
				}
				// 前の曲ボタンが押された場合（ID順で前の曲を取得）
				else if (prevStr != null && !prevStr.isEmpty()) {
					int currentId = Integer.parseInt(prevStr);
					music = service.getPrevMusicById(currentId);
				}
				// URLパラメータが指定された場合
				else if (urlStr != null && !urlStr.isEmpty()) {
					music = service.getMusicByUrl(urlStr);
				}
				// IDパラメータが指定された場合（従来通り）
				else if (idStr != null && !idStr.isEmpty()) {
					int id = Integer.parseInt(idStr);
					music = service.getMusic(id);
				}

				// ID順のリストをセッションに保存（次の曲ボタン用）
				List<Music> musicListByIdOrder = service.getMusicListByIdOrder();
				session.setAttribute("musicListByIdOrder", musicListByIdOrder);
				request.setAttribute("isBookmarkMode", false);
			}

			if (music == null) {
				// 曲が見つからない場合はトップに戻る
				response.sendRedirect(request.getContextPath() + "/PlayMusic");
				return;
			}
			request.setAttribute("music", music);

			// ブックマーク状態を取得
			User user = new User(userName, "");
			BookmarkDAO bookmarkDAO = new BookmarkDAO();
			boolean isBookmarked = bookmarkDAO.isBookmarked(user, music);
			request.setAttribute("isBookmarked", isBookmarked);

			// フォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/playMusic.jsp");
			dispatcher.forward(request, response);
			System.out.println("曲とばすことはでけた！");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
