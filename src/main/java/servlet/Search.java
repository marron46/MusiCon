package servlet;

import java.io.IOException;

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

		// リクエストパラメータを取得
		request.setCharacterEncoding("UTF-8");
		String searchText = request.getParameter("searchText");

		//曲の検索処理
		Music musicSearch = new Music(searchText);
		SearchResultLogic logic = new SearchResultLogic();
		boolean result = logic.execute(musicSearch);

		// 曲検索処理の成否によって処理を分岐
		if (result) { // 曲検索成功時
			// セッションスコープに曲タイトルを保存
			HttpSession session = request.getSession();
			session.setAttribute("searchText", searchText);
			// フォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("searchResult.jsp");
			dispatcher.forward(request, response);
			System.out.print("でけた！");
		} else { // 曲検索失敗時
			// リダイレクト
			response.sendRedirect("Search");
			System.out.print("ろぐいんできない");
		}
	}
}
