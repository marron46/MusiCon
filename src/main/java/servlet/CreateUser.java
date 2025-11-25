package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.User;
import model.logic.CreateUserLogic;

@WebServlet("/CreateUser")
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
		String user_name = request.getParameter("name");
		String user_pass = request.getParameter("pass");

		// ログイン処理の実行
		User user = new User(user_name, user_pass);
		CreateUserLogic logic = new CreateUserLogic();
		boolean result = logic.execute(user);

		// ログイン処理の成否によって処理を分岐
		if (result) { // ログイン成功時
			// セッションスコープにユーザーIDを保存
			HttpSession session = request.getSession();
			session.setAttribute("user_name", user_name);
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
