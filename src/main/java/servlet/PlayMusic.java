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
import model.Music;
import model.logic.PlayMusicLogic;

@WebServlet("/PlayMusic")
public class PlayMusic extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		PlayMusicLogic logic = new PlayMusicLogic();

		String idStr = request.getParameter("id");
		System.out.println("idStr" + idStr);

		if (idStr == null) {
			// ログインチェック
			HttpSession session = request.getSession();
			String userName = (String) session.getAttribute("user_name");
			if (userName == null) {
				response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
				return;
			}
			// 曲一覧を取得して JSP へ
			List<Music> musicList = logic.getMusicList();
			// セッションスコープに保存
			session.setAttribute("musicList", musicList);
			System.out.println("DAOからとってきた曲リスト" + musicList);
			// フォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/top.jsp");
			dispatcher.forward(request, response);
			System.out.println("曲がないのでtopに戻ります");

		} else {
			// 1曲取得
			//int id = Integer.parseInt(idStr);
			HttpSession session = request.getSession();
			String userName = (String) session.getAttribute("user_name");
			if (userName == null) {
				response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
				return;
			}
			int id = Integer.parseInt(request.getParameter("id"));
			Music music = logic.getMusic(id);
			request.setAttribute("music", music);

			// フォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/playMusic.jsp");
			dispatcher.forward(request, response);
			System.out.println("曲とばすことはでけた！");
		}

		/*int id = Integer.parseInt(idStr);
		
		// Logic 呼び出し
		Music music = logic.getMusic(id);
		System.out.println("id" + music);
		
		// music が null ならトップへ戻す
		if (music == null) {
			response.sendRedirect("PlayMusic");
			return;
		}
		
		// JSP に渡す
		request.setAttribute("music", music);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("playMusic.jsp");
		dispatcher.forward(request, response);*/

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
