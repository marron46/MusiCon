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

		System.out.println("CreateUser作動");
		// リクエストパラメータを取得
		request.setCharacterEncoding("UTF-8");
		String user_name = request.getParameter("user_name");
		String Hr_user_pass = request.getParameter("user_pass");

		if (Hr_user_pass == null || Hr_user_pass.isEmpty()) {
			System.out.println("値が空です");
			return;
		}
		// ハッシュ化
		String user_pass = hashSHA256(Hr_user_pass);

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
