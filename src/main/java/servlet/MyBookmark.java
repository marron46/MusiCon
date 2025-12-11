package servlet;

import java.io.IOException;
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
import service.MyBookmarkService;

@WebServlet("/MyBookmark")
public class MyBookmark extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		
		MyBookmarkService service = new MyBookmarkService();

		// ログインユーザーを取得
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("user_name");
		if (userName == null) {
			response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
			return;
		}
		User user = new User(userName, ""); // パスワードはブックマーク処理では不要

		// クリックされたMusic IDを取得
		String musicIdStr = request.getParameter("id");

		// idパラメータがある場合：ブックマークを追加
		if (musicIdStr != null) {
			int musicId = Integer.parseInt(musicIdStr);

			// Music情報取得
			MusicDAO musicDAO = new MusicDAO();
			Music music = musicDAO.playMusicById(musicId);

			
			boolean result = service.execute(user, music);

			// 結果メッセージを設定
			if (result) {
				request.setAttribute("message", "ブックマークに追加しました！");
			} else {
				request.setAttribute("message", "すでに登録済みです。");
			}

			System.out.println("resultに" + result + "が返ってきている");
		}

		// ブックマーク一覧を取得（INDEX順）
		BookmarkDAO bookmarkDAO = new BookmarkDAO();
		List<Bookmark> bookmarkList = service.getBookmark(user);
		System.out.println("Servletでブックマーク出力:" + bookmarkList);
		

		// セッションにブックマーク一覧を保存（INDEX順でソート済み）
		session.setAttribute("bookmarkList", bookmarkList);

		// ブックマーク一覧ページにフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/myBookmark.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		// ログインユーザーを取得
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("user_name");
		if (userName == null) {
			response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
			return;
		}
		User user = new User(userName, ""); // パスワードはブックマーク処理では不要

		// クリックされたMusic IDを取得
		String musicIdStr = request.getParameter("id");
		if (musicIdStr != null) {
			int musicId = Integer.parseInt(musicIdStr);

			// Music情報取得
			MusicDAO musicDAO = new MusicDAO();
			Music music = musicDAO.playMusicById(musicId);
			
			// musicがnullの場合はエラー
			if (music == null) {
				response.sendRedirect(request.getContextPath() + "/PlayMusic");
				return;
			}

			MyBookmarkService service = new MyBookmarkService();
			// toggleBookmarkを使用して追加/削除を切り替え
			boolean result = service.toggleBookmark(user, music);

			// ブックマークモードかどうかを確認
			String bookmarkMode = request.getParameter("bookmarkMode");
			String bookmarkIndex = request.getParameter("bookmarkIndex");
			
			// リダイレクト先を決定
			String redirectUrl;
			if ("true".equals(bookmarkMode) && bookmarkIndex != null) {
				// ブックマークモードの場合
				redirectUrl = request.getContextPath() + "/PlayMusic?bookmarkMode=true&bookmarkIndex=" + bookmarkIndex;
			} else {
				// 通常再生モードの場合
				redirectUrl = request.getContextPath() + "/PlayMusic?id=" + musicId;
			}
			
			response.sendRedirect(redirectUrl);
		}

	}

}
