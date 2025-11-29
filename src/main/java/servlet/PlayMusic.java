package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Music;
import model.logic.PlayMusicLogic;

@WebServlet("/PlayMusic")
public class PlayMusic extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PlayMusicLogic logic = new PlayMusicLogic();

		String idStr = request.getParameter("id");

		if (idStr == null) {
			// フォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("top.jsp");
			dispatcher.forward(request, response);
			System.out.println("曲がないのでtopに戻ります");
			
		} else {
			// 1曲取得
			int id = Integer.parseInt(idStr);
			Music music = logic.getMusic(id);
			request.setAttribute("music", music);

			// フォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("playMusic.jsp");
			dispatcher.forward(request, response);
			System.out.print("曲とばすことはでけた！");
		}

	}
}
