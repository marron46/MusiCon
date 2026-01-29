package servlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;

/**
 * MP3に埋め込まれたジャケット画像（Artwork）を配信するサーブレット
 */
@WebServlet("/Jacket")
public class Jacket extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Object user = session.getAttribute("user_name");
		if (user == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "ログインが必要です");
			return;
		}

		String fileName = request.getParameter("file");
		if (fileName == null || fileName.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "fileパラメータが必要です");
			return;
		}

		try {
			fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8.name());
		} catch (Exception e) {
			// デコード失敗時はそのまま使用
		}

		if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なファイル名です");
			return;
		}

		String uploadPath = System.getProperty("user.home") + File.separator + "musicon-music";
		File musicDir = new File(uploadPath);
		if (!musicDir.exists() || !musicDir.isDirectory()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "音楽ディレクトリが見つかりません");
			return;
		}

		File musicFile = new File(musicDir, fileName);
		if (!musicFile.exists() || !musicFile.isFile()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "ファイルが見つかりません");
			return;
		}

		try {
			AudioFile audioFile = AudioFileIO.read(musicFile);
			Tag tag = audioFile.getTag();
			if (tag == null) {
				sendNoArtwork(response);
				return;
			}

			Artwork artwork = tag.getFirstArtwork();
			if (artwork == null || artwork.getBinaryData() == null || artwork.getBinaryData().length == 0) {
				sendNoArtwork(response);
				return;
			}

			byte[] data = artwork.getBinaryData();
			String mimeType = artwork.getMimeType();
			if (mimeType == null || mimeType.isEmpty()) {
				mimeType = "image/jpeg";
			}

			response.setContentType(mimeType);
			response.setContentLength(data.length);
			response.setHeader("Cache-Control", "private, max-age=3600");

			try (OutputStream out = response.getOutputStream()) {
				out.write(data);
			}
		} catch (Exception e) {
			System.err.println("ジャケット読み取り失敗: " + fileName + " - " + e.getMessage());
			sendNoArtwork(response);
		}
	}

	private void sendNoArtwork(HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND, "ジャケット画像がありません");
	}
}
