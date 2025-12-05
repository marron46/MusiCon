package servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.Music;
import model.logic.ImportMusicLogic;
import model.logic.PlayMusicLogic;

@WebServlet("/ImportMusic")

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 100, // 100MB までOK
		maxRequestSize = 1024 * 1024 * 150 // リクエスト全体で150MBまで
)

public class ImportMusic extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// ログインチェック
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("user_name");
		if (userName == null) {
			response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
			return;
		}

		PlayMusicLogic logic = new PlayMusicLogic();
		// 曲一覧を取得して JSP へ
		List<Music> musicList = logic.getMusicList();
		session.setAttribute("musicList", musicList);
		System.out.println("DAOからとってきた曲リスト" + musicList);
		// フォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/top.jsp");
		dispatcher.forward(request, response);
		System.out.println("id順に曲リストを作ってtopに戻ります");

		// フォワード
		/*RequestDispatcher dispatcher = request.getRequestDispatcher("importMusic.jsp");
		dispatcher.forward(request, response);*/

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// ログインチェック
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("user_name");
		if (userName == null) {
			response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
			return;
		}

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
		Part filePart = request.getPart("file"); // アップロードされたファイル

		// int型に変換
		int releaseYMD = Integer.parseInt(Str_releaseYMD);
		int music_time = Integer.parseInt(Str_music_time);

		// 保存フォルダ（webapps/プロジェクト/music/）
		String uploadPath = request.getServletContext().getRealPath("music"); // 「音楽ファイルを保存するフォルダの実際のディスク上の場所」を取得
		File uploadDir = new File(uploadPath); // 「保存先フォルダ」を表す File オブジェクトを作る（作るとは言ってないただ”場所”を示すだけ）
		if (!uploadDir.exists())
			uploadDir.mkdir(); // 「music フォルダが無ければ作る」

		ImportMusicLogic logic = new ImportMusicLogic();

		// ファイル名取得
		String fileName = filePart.getSubmittedFileName();
		String savePath = uploadPath + File.separator + fileName;

		// 実際にファイル保存
		filePart.write(savePath);

		// DBに登録するファイルパス（JSP側の <audio> で使うパス）
		String dbFilePath = "music/" + fileName;

		// DAO → DB登録
		logic.addMusic(title, genre, artist, lyricist, composer, releaseYMD, music_time, dbFilePath);

		// フォワード
		/*RequestDispatcher dispatcher = request.getRequestDispatcher("top.jsp");// のちにフォワード先をPlayMusicのservletに変更
		dispatcher.forward(request, response);*/
		//response.sendRedirect("PlayMusic");
		doGet(request, response);
	}
}