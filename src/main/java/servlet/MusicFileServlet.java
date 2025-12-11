package servlet;

import java.io.File;
import java.io.FileInputStream;
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

/**
 * MP3ファイルを配信するサーブレット
 */
@WebServlet("/MusicFile")
public class MusicFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// ログインチェック
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("user_name");
		if (userName == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "ログインが必要です");
			return;
		}

		// パラメータからファイル名を取得（URLデコード）
		String fileName = request.getParameter("file");
		if (fileName == null || fileName.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ファイル名が指定されていません");
			return;
		}

		// URLデコードを明示的に実行（UTF-8）
		try {
			fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8.name());
		} catch (Exception e) {
			// デコードに失敗した場合は元のファイル名を使用
			System.err.println("ファイル名のデコードに失敗しました: " + fileName);
		}

		// セキュリティ対策：ファイル名にパス区切り文字が含まれていないかチェック
		// デコード後のファイル名をチェック
		if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なファイル名です");
			return;
		}

		// ファイルの実際のパスを取得（デプロイに依存しないユーザーHOME配下を優先）
		String uploadPath = System.getProperty("user.home") + File.separator + "musicon-music";
		File musicDir = new File(uploadPath);
		if (!musicDir.exists() || !musicDir.isDirectory()) {
			// フォールバック（古い構成用）：webapp/music
			String realPath = request.getServletContext().getRealPath("music");
			if (realPath == null) {
				realPath = System.getProperty("user.dir") + File.separator + "music";
			}
			musicDir = new File(realPath);
		}
		
		if (!musicDir.exists() || !musicDir.isDirectory()) {
			System.err.println("音楽ディレクトリが見つかりません: " + musicDir.getAbsolutePath());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "音楽ディレクトリが見つかりません");
			return;
		}
		
		File musicFile = new File(musicDir, fileName);

		// ファイルが存在しない場合、デバッグ情報を出力
		if (!musicFile.exists() || !musicFile.isFile()) {
			System.err.println("ファイルが見つかりません:");
			System.err.println("  要求されたファイル名: " + fileName);
			System.err.println("  検索パス: " + musicFile.getAbsolutePath());
			System.err.println("  ディレクトリ内容: ");
			if (musicDir.exists() && musicDir.isDirectory()) {
				File[] files = musicDir.listFiles();
				if (files != null) {
					for (File f : files) {
						System.err.println("    - " + f.getName());
					}
				}
			}
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "ファイルが見つかりません: " + fileName);
			return;
		}

		// ファイルの拡張子をチェック（MP3ファイルのみ許可）
		String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
		if (!fileExtension.equals("mp3")) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "MP3ファイルのみ許可されています");
			return;
		}

		long fileLength = musicFile.length();
		long start = 0;
		long end = fileLength - 1;
		
		// Rangeリクエストの処理（HTML5 audioタグのストリーミング再生に対応）
		String rangeHeader = request.getHeader("Range");
		if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
			// Rangeヘッダーを解析
			String[] ranges = rangeHeader.substring(6).split("-");
			try {
				if (ranges[0].length() > 0) {
					start = Long.parseLong(ranges[0]);
				}
				if (ranges.length > 1 && ranges[1].length() > 0) {
					end = Long.parseLong(ranges[1]);
				}
				// 範囲の検証
				if (start < 0 || end >= fileLength || start > end) {
					response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
					response.setHeader("Content-Range", "bytes */" + fileLength);
					return;
				}
			} catch (NumberFormatException e) {
				// 無効なRangeヘッダーの場合は全ファイルを返す
				start = 0;
				end = fileLength - 1;
			}
		}
		
		long contentLength = end - start + 1;

		// レスポンスヘッダーを設定
		response.setContentType("audio/mpeg");
		response.setHeader("Accept-Ranges", "bytes");
		response.setHeader("Content-Length", String.valueOf(contentLength));
		response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
		
		// Rangeリクエストの場合は206 Partial Contentを返す
		if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
			response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
			response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
		} else {
			response.setStatus(HttpServletResponse.SC_OK);
		}

		// ファイルを読み込んでレスポンスに書き込む
		try (FileInputStream fis = new FileInputStream(musicFile);
			 OutputStream out = response.getOutputStream()) {
			
			// 指定された範囲までスキップ
			if (start > 0) {
				fis.skip(start);
			}
			
			byte[] buffer = new byte[8192];
			long remaining = contentLength;
			int bytesRead;
			
			while (remaining > 0 && (bytesRead = fis.read(buffer, 0, (int) Math.min(buffer.length, remaining))) != -1) {
				out.write(buffer, 0, bytesRead);
				remaining -= bytesRead;
			}
			out.flush();
		} catch (IOException e) {
			// クライアントが接続を切断した場合などは無視
			if (!response.isCommitted()) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ファイルの読み込みに失敗しました");
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
