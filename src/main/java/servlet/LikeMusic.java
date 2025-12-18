package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Music;
import service.PlayMusicService;

@WebServlet("/LikeMusic")
public class LikeMusic extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		boolean isAjax = "true".equalsIgnoreCase(request.getParameter("ajax"))
				|| "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));

		// ログインチェック
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("user_name");
		if (userName == null) {
			if (isAjax) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setContentType("application/json; charset=UTF-8");
				try (PrintWriter out = response.getWriter()) {
					out.print("{\"success\":false,\"error\":\"UNAUTHORIZED\"}");
				}
				return;
			}
			response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
			return;
		}

		// リクエストパラメータを取得
		request.setCharacterEncoding("UTF-8");
		String idStr = request.getParameter("id");
		int id;
		try {
			id = Integer.parseInt(idStr);
		} catch (Exception e) {
			if (isAjax) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setContentType("application/json; charset=UTF-8");
				try (PrintWriter out = response.getWriter()) {
					out.print("{\"success\":false,\"error\":\"INVALID_ID\"}");
				}
				return;
			}
			response.sendRedirect(request.getContextPath() + "/PlayMusic");
			return;
		}

		PlayMusicService service = new PlayMusicService();
		service.likeMusic(id);

		// AJAX の場合は画面遷移させず、更新後の likes を返す
		if (isAjax) {
			Music updated = service.getMusic(id);
			int likes = updated == null ? -1 : updated.getLikes();
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json; charset=UTF-8");
			try (PrintWriter out = response.getWriter()) {
				out.print("{\"success\":true,\"likes\":" + likes + "}");
			}
			return;
		}

		// 更新後、同じ曲ページへ戻る（リダイレクト）
		response.sendRedirect(request.getContextPath() + "/PlayMusic?id=" + id);

	}

}
