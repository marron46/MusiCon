package servlet;

import java.io.IOException;
import java.util.List;

import dao.BookmarkDAO;
import dao.MusicDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Bookmark;
import model.Music;
import model.User;
import model.logic.MyBookmarkLogic;

@WebServlet("/MyBookmark")
public class MyBookmark extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
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

		// idパラメータがある場合：ブックマークを追加
		if (musicIdStr != null) {
			int musicId = Integer.parseInt(musicIdStr);

			// Music情報取得
			MusicDAO musicDAO = new MusicDAO();
			Music music = musicDAO.playMusicById(musicId);

			MyBookmarkLogic logic = new MyBookmarkLogic();
			boolean result = logic.execute(user, music);

			// 結果メッセージを設定
			if (result) {
				request.setAttribute("message", "ブックマークに追加しました！");
			} else {
				request.setAttribute("message", "すでに登録済みです。");
			}

			System.out.println("resultに" + result + "が返ってきている");
		}

		// ブックマーク一覧を取得
		BookmarkDAO bookmarkDAO = new BookmarkDAO();
		List<Bookmark> bookmarkList = bookmarkDAO.getBookmark(user);

		// セッションにブックマーク一覧を保存
		session.setAttribute("bookmarkList", bookmarkList);

		// ブックマーク一覧ページにフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/myBookmark.jsp");
		dispatcher.forward(request, response);
	}

}
