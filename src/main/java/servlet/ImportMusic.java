package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Music;
import model.logic.ImportMusicLogic;

@WebServlet("/ImportMusic")
public class ImportMusic extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// リクエストパラメータを取得
		// releaseYearをint型に変換 + 残りの宣言
		request.setCharacterEncoding("UTF-8");
		String title = request.getParameter("title");
		String genre = request.getParameter("genre");
		String artist = request.getParameter("artist");
		String lyricist = request.getParameter("lyricist");
		String composer = request.getParameter("composer");
		String Str_releaseYMD = request.getParameter("releaseYMD");
		String Str_music_time = request.getParameter("music_time");
		String Str_like = request.getParameter("like");
		String url = request.getParameter("url");
		
		// releaseYearをint型に変換 + 残りの宣言
		int releaseYMD = Integer.parseInt(Str_releaseYMD);
		int music_time = Integer.parseInt(Str_music_time);

		// 曲インポート処理の実行
		Music music = new Music(title,genre,artist,composer,lyricist,releaseYMD,music_time,url);
		ImportMusicLogic logic = new ImportMusicLogic();
		boolean result = logic.execute(music);
		System.out.println(music);

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
			System.out.print("曲入らない");
		}
	}
}
