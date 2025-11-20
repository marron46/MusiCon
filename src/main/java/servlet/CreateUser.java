package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("CreateUser")
public class CreateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// フォワード
				RequestDispatcher dispatcher = request.getRequestDispatcher("createUser.jsp");
				dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// リクエストパラメータを取得
		request.setCharacterEncoding("UTF-8");
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");

		// ログイン処理の実行
		User user = new User(name, pass);
		CreateUserLogic logic = new CreateUserLogic();
		boolean result = logic.execute(user);

		// ログイン処理の成否によって処理を分岐
		if (result) { // ログイン成功時
			// セッションスコープにユーザーIDを保存
			HttpSession session = request.getSession();
			session.setAttribute("name", name);
			// フォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("createResult.jsp");
			dispatcher.forward(request, response);
			System.out.print("でけた！");
		} else { // ログイン失敗時
			// リダイレクト
			response.sendRedirect("CreateUser");
			System.out.print("ろぐいんできない");
		}
	}
}
