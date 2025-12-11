package servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
import service.ImportMusicService;
import service.PlayMusicService;

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

		PlayMusicService service = new PlayMusicService();

		// 曲一覧を取得して JSP へ
		List<Music> musicList = service.getMusicList();
		session.setAttribute("musicList", musicList);
		System.out.println("DAOからとってきた曲リスト" + musicList);

		// フォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/top.jsp");
		dispatcher.forward(request, response);
		System.out.println("id順に曲リストを作ってtopに戻ります");
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

		// 保存フォルダ：永続ディレクトリに保存（デプロイの影響を受けないようにユーザーHOME配下に作成）
		String uploadPath = System.getProperty("user.home") + File.separator + "musicon-music";
		File uploadDir = new File(uploadPath);
		// ディレクトリが存在しない場合は作成
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		ImportMusicService service = new ImportMusicService();

		// 元のファイル名から拡張子を取得
		String originalFileName = filePart.getSubmittedFileName();
		String fileExtension = "";
		if (originalFileName != null && originalFileName.contains(".")) {
			fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
		}

		// UUIDでランダムなファイル名を生成（拡張子は保持）
		String randomFileName = UUID.randomUUID().toString() + fileExtension;
		String savePath = uploadPath + File.separator + randomFileName;

		// 実際にファイル保存
		filePart.write(savePath);

		// DBに登録するファイルパス（サーブレット経由で配信するURL）
		// ランダムなファイル名を使用するため、URLエンコードは不要（英数字とハイフンのみ）
		// コンテキストパスは含めず、相対パスのみ保存（JSPでコンテキストパスを追加する）
		String dbFilePath = "/MusicFile?file=" + randomFileName;

		// DAO → DB登録
		service.addMusic(title, genre, artist, lyricist, composer, releaseYMD, music_time, dbFilePath);

		// フォワード
		doGet(request, response);
	}
}

