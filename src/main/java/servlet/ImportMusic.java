package servlet;

import java.io.IOException;
import java.sql.Time;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Music;

@WebServlet("ImportMusic")
public class ImportMusic extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// リクエストパラメータを取得
		request.setCharacterEncoding("UTF-8");
		String title = request.getParameter("title");
		String artist = request.getParameter("artist");
		String SreleaseYear = request.getParameter("releaseYear");
		
		// releaseYearをint型に変換 + 残りの宣言
		int releaseYear = Integer.parseInt(SreleaseYear);
		String genre;
		String lyricist;
		String composer;
		int like;
		Time music_time;

		// 曲インポート処理の実行
		Music music = new Music(title, genre, artist, lyricist, composer, releaseYear, like, music_time);
		ImportMusicLogic logic = new ImportMusicLogic();
		boolean result = logic.execute(music);

		// 曲インポート処理の成否によって処理を分岐
		if (result) { // 曲インポート成功時
			// セッションスコープに曲タイトルを保存
			HttpSession session = request.getSession();
			session.setAttribute("title", title);
			// フォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("top.jsp");
			dispatcher.forward(request, response);
			System.out.print("でけた！");
		} else { // 曲インポート失敗時
			// リダイレクト
			response.sendRedirect("ImportMusic");
			System.out.print("ろぐいんできない");
		}
	}
}
