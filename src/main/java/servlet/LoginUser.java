package servlet;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.User;
import model.logic.LoginUserLogic;

@WebServlet("/LoginUser")
public class LoginUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// フォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/login.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// リクエストパラメータを取得
		request.setCharacterEncoding("UTF-8");
		String user_name = request.getParameter("user_name");
		String Hr_user_pass = request.getParameter("user_pass");

		if (Hr_user_pass == null || Hr_user_pass.isEmpty()) {
			System.out.println("値が空です");
			request.setAttribute("loginError", "パスワードが空です");
			request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
			return;
		}
		// ハッシュ化
		String user_pass = hashSHA256(Hr_user_pass);

		// ログイン処理の実行
		User user = new User(user_name, user_pass);
		LoginUserLogic logic = new LoginUserLogic();
		User result = logic.execute(user);
		System.out.println("Servlet result:" + result);

		if (result != null) {
			user = result;
			HttpSession session = request.getSession();
			session.setAttribute("user_name", user_name);
			session.setAttribute("loginUser", user);
			// PlayMuzicページへリダイレクト
			response.sendRedirect(request.getContextPath() + "/PlayMusic");
			System.out.print("ログインでけた！");
		} else {
			request.setAttribute("errorMessage", "ユーザー名またはパスワードが違います。");
			// ログイン画面に戻す
			request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
			System.out.print("ログインできない");
		}
	}

	// SHA-256 ハッシュ関数
	private String hashSHA256(String input) {
		try {
			// Java の MessageDigest クラスを使い、SHA-256 アルゴリズムを使うインスタンスを作成
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			// 入力文字列を byte[] に変換し、それをまとめて digest() に渡してハッシュを計算。hash は ハッシュ値のバイト配列（32バイト）。
			byte[] hash = digest.digest(input.getBytes());

			// ハッシュ結果を人間が読める形にするため、16進数文字列に変換
			StringBuilder hexString = new StringBuilder();

			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
