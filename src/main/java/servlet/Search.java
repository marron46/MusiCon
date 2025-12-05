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
import model.logic.SearchResultLogic;

@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// ログインチェック
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("user_name");
		if (userName == null) {
			response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
			return;
		}

		// リクエストパラメータを取得
		request.setCharacterEncoding("UTF-8");
		String searchText = request.getParameter("searchText");

		//曲の検索処理
		Music musicSearch = new Music(searchText);
		SearchResultLogic logic = new SearchResultLogic();
		List<Music> result = logic.execute(musicSearch);

		// セッションスコープに曲タイトルを保存
		session.setAttribute("searchList", result);
		System.out.println("検索結果" + result);
		// フォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/searchResult.jsp");
		dispatcher.forward(request, response);
		System.out.print("でけた！");

	}
}
