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

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

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

		// フォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/top.jsp");
		dispatcher.forward(request, response);
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

		request.setCharacterEncoding("UTF-8");

		// 手動登録モード：メタデータ取得失敗後にユーザーが入力した内容で登録
		if ("1".equals(request.getParameter("manual"))) {
			String title = request.getParameter("title");
			String artist = request.getParameter("artist");
			String genre = request.getParameter("genre");
			String savedFile = request.getParameter("saved_file");
			int releaseY = 0;
			int musicTime = 0;
			try {
				String ry = request.getParameter("releaseYear");
				if (ry != null && !ry.isEmpty()) releaseY = Integer.parseInt(ry.trim());
			} catch (NumberFormatException ignored) {}
			try {
				String mt = request.getParameter("music_time");
				if (mt != null && !mt.isEmpty()) musicTime = Integer.parseInt(mt.trim());
			} catch (NumberFormatException ignored) {}

			if (title != null && !title.isEmpty() && artist != null && !artist.isEmpty()
					&& genre != null && !genre.isEmpty() && savedFile != null && !savedFile.isEmpty()) {
				ImportMusicService service = new ImportMusicService();
				String dbFilePath = "/MusicFile?file=" + savedFile;
				service.addMusic(title, genre, artist, releaseY, musicTime, dbFilePath);
			}
			response.sendRedirect(request.getContextPath() + "/PlayMusic");
			return;
		}

		// 通常モード：ファイル＋ジャンルでアップロードし、メタデータを自動取得
		String genre = request.getParameter("genre");
		Part filePart = request.getPart("file");
		if (filePart == null || filePart.getSize() == 0 || genre == null || genre.isEmpty()) {
			request.setAttribute("importError", "ファイルとジャンルを選択してください。");
			RequestDispatcher rd = request.getRequestDispatcher("jsp/importMusic.jsp");
			rd.forward(request, response);
			return;
		}

		String uploadPath = System.getProperty("user.home") + File.separator + "musicon-music";
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		String originalFileName = filePart.getSubmittedFileName();
		String fileExtension = "";
		if (originalFileName != null && originalFileName.contains(".")) {
			fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
		}

		String randomFileName = UUID.randomUUID().toString() + fileExtension;
		String savePath = uploadPath + File.separator + randomFileName;
		filePart.write(savePath);

		// メタデータを自動取得
		String title = "";
		String artist = "";
		int releaseY = 0;
		int musicTime = 0;
		String metadataError = null;
		try {
			AudioFile audioFile = AudioFileIO.read(new File(savePath));
			Tag tag = audioFile.getTag();
			if (tag != null) {
				title = tag.getFirst(FieldKey.TITLE);
				if (title == null) title = "";
				artist = tag.getFirst(FieldKey.ARTIST);
				if (artist == null) artist = "";
				String year = tag.getFirst(FieldKey.YEAR);
				if (year != null && !year.isEmpty()) {
					try { releaseY = Integer.parseInt(year.trim()); } catch (NumberFormatException ignored) {}
				}
			}
			if (audioFile.getAudioHeader() != null) {
				musicTime = audioFile.getAudioHeader().getTrackLength();
			}
		} catch (Exception e) {
			metadataError = "メタデータを取得できませんでした。以下の項目を手動で入力してください。（" + e.getMessage() + "）";
			System.err.println("メタデータ読み取り時の例外: " + e.getMessage());
		}

		// 必須項目（タイトル・アーティスト）が取れていない場合も手動登録へ
		if (metadataError == null && (title == null || title.isEmpty() || artist == null || artist.isEmpty())) {
			metadataError = "メタデータにタイトルまたはアーティストが含まれていません。手動で入力してください。";
		}

		if (metadataError != null) {
			// 再生時間のみ再度取得を試行（タグは読めなくてもヘッダーは読める場合がある）
			try {
				AudioFile af = AudioFileIO.read(new File(savePath));
				if (af.getAudioHeader() != null) musicTime = af.getAudioHeader().getTrackLength();
			} catch (Exception ignored) {}
			String prefilledTitle = (originalFileName != null && originalFileName.contains("."))
				? originalFileName.substring(0, originalFileName.lastIndexOf(".")) : "";
			request.setAttribute("metadataError", metadataError);
			request.setAttribute("saved_file", randomFileName);
			request.setAttribute("genre", genre);
			request.setAttribute("prefillTitle", prefilledTitle);
			request.setAttribute("prefillArtist", artist != null ? artist : "");
			request.setAttribute("prefillReleaseYear", releaseY);
			request.setAttribute("prefillMusicTime", musicTime);
			RequestDispatcher rd = request.getRequestDispatcher("jsp/importMusicManual.jsp");
			rd.forward(request, response);
			return;
		}

		ImportMusicService service = new ImportMusicService();
		String dbFilePath = "/MusicFile?file=" + randomFileName;
		service.addMusic(title, genre, artist, releaseY, musicTime, dbFilePath);
		response.sendRedirect(request.getContextPath() + "/PlayMusic");
	}
}

