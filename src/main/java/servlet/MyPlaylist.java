package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Music;
import model.PlaylistItem;
import model.User;
import service.MyPlaylistService;

@WebServlet("/MyPlaylist")
public class MyPlaylist extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();

		String userName = (String) session.getAttribute("user_name");
		if (userName == null) {
			response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
			return;
		}

		User user = new User(userName, "");
		MyPlaylistService service = new MyPlaylistService();

		List<PlaylistItem> playlistItems = service.getPlaylist(user);
		session.setAttribute("playlistItems", playlistItems);

		RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/myPlaylist.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("user_name");
		boolean isAjax = "true".equalsIgnoreCase(request.getParameter("ajax"))
				|| "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
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

		String musicIdStr = request.getParameter("id");
		if (musicIdStr == null || musicIdStr.isEmpty()) {
			if (isAjax) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setContentType("application/json; charset=UTF-8");
				try (PrintWriter out = response.getWriter()) {
					out.print("{\"success\":false,\"error\":\"MISSING_ID\"}");
				}
				return;
			}
			response.sendRedirect(request.getContextPath() + "/MyPlaylist");
			return;
		}

		int musicId = Integer.parseInt(musicIdStr);
		User user = new User(userName, "");
		MyPlaylistService service = new MyPlaylistService();
		// プレイリスト登録/解除は musicId だけで扱える（DBのMUSICS存在チェックはDAOのFKで担保）
		// ここでは単純にtoggleする
		service.toggle(user, new Music(musicId, "", "", 0, ""));

		// AJAX の場合は画面遷移させず、現在状態を JSON で返す
		if (isAjax) {
			boolean nowInPlaylist = service.isInPlaylist(user, new Music(musicId, "", "", 0, ""));
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json; charset=UTF-8");
			try (PrintWriter out = response.getWriter()) {
				out.print("{\"success\":true,\"inPlaylist\":" + nowInPlaylist + "}");
			}
			return;
		}

		// 再生画面から来た場合はプレイリスト再生へ戻す
		String playlistMode = request.getParameter("playlistMode");
		String playlistPos = request.getParameter("playlistPos");
		if ("true".equals(playlistMode)) {
			if (playlistPos != null && !playlistPos.isEmpty()) {
				response.sendRedirect(request.getContextPath() + "/PlayMusic?playlistMode=true&pos=" + playlistPos);
			} else {
				response.sendRedirect(request.getContextPath() + "/PlayMusic?playlistMode=true");
			}
			return;
		}

		response.sendRedirect(request.getContextPath() + "/MyPlaylist");
	}
}


