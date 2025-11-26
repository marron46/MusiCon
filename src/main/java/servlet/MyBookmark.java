package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Music;
import model.logic.MyBookmarkLogic;

@WebServlet("/MyBookmark")
public class MyBookmark extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		MyBookmarkLogic logic = new MyBookmarkLogic();
		// いいねランキングを取得
		List<Music> bookmark = logic.getBookmark();
		// JSP へ渡す
		request.setAttribute("bookmark", bookmark);
		// フォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("MyBookmark.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
