package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Bookmark;
import model.User;
import model.logic.MyBookmarkLogic;

@WebServlet("/BookmarkPlay")
public class BookmarkPlay extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("user_name");
		if (userName == null) {
			response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
			return;
		}
		User user = new User(userName, "");
		
		MyBookmarkLogic logic = new MyBookmarkLogic();
		
		// 全音源リスト（すでに表示に使っている想定）
		List<Bookmark> bookmarkList = logic.getBookmark(user);
		System.out.println("Servletでブックマーク出力:" + bookmarkList);
		int id = Integer.parseInt(request.getParameter("id"));
		String mode = request.getParameter("mode");

		int index = -1;

		// 現在位置を探す
		for(int i = 0; i < bookmarkList.size(); i++) {
			if(bookmarkList.get(i).getMusic_id() == id) {
				index = i;
				break;
			}
		}

		// next/prevの処理
		if(mode != null) {
			if(mode.equals("next") && index < bookmarkList.size() - 1) {
				index++;
			} else if(mode.equals("prev") && index > 0) {
				index--;
			}
		}

		Bookmark Bookmarkmusic = bookmarkList.get(index);

		// JSPへ渡す
		request.setAttribute("Bookmarkmusic", Bookmarkmusic);
		request.getRequestDispatcher("jsp/playBookmark.jsp").forward(request, response);
	}

}
