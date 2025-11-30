package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.logic.PlayMusicLogic;

public class LikeMusic extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// リクエストパラメータを取得
		request.setCharacterEncoding("UTF-8");
		int id = Integer.parseInt(request.getParameter("id"));

		PlayMusicLogic logic = new PlayMusicLogic();
		logic.likeMusic(id);

		// 更新後、同じ曲ページへ戻る（リダイレクト）
		response.sendRedirect("PlayMusic?id=" + id);

	}

}
