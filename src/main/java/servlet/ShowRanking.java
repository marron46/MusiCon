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
import service.ShowRankingService;

@WebServlet("/ShowRanking")
public class ShowRanking extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// ログインチェック
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("user_name");
		if (userName == null) {
			response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
			return;
		}

		ShowRankingService service = new ShowRankingService();
		// いいねランキングを取得
		List<Music> ranking = service.getRanking();
		System.out.println("ランキング出力:" + ranking);
		// セッションスコープにランキングリストを保存
		session.setAttribute("ranking", ranking);
		// フォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/showRanking.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
