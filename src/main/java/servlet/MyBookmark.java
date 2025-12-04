package servlet;

import java.io.IOException;

import dao.MusicDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
		User user = (User) session.getAttribute("user_name");

		// クリックされたMusic IDを取得
		String musicIdStr = request.getParameter("id");
		if (musicIdStr == null) {
			System.out.println("topに行こうとしている");
			response.sendRedirect("top.jsp");
			return;
		}

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
		
		System.out.println("resultに"+result+"が返ってきている");

		request.getRequestDispatcher("myBookmark.jsp").forward(request, response);
	}

}
