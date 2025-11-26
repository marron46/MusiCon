package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Music;
import model.logic.ShowRankingLogic;

@WebServlet("/ShowRanking")
public class ShowRanking extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ShowRankingLogic logic = new ShowRankingLogic();
		// いいねランキングを取得
		List<Music> ranking = logic.getRanking();
		// JSP へ渡す
		request.setAttribute("ranking", ranking);
		// フォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("showRanking.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
