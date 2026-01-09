package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Music;
import model.User;
import dao.PlaylistDAO;
import service.PlayMusicService;

@WebServlet({"/PlayMusic"})
public class PlayMusic extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		PlayMusicService service = new PlayMusicService();

		String idStr = request.getParameter("id");
		String urlStr = request.getParameter("url");
		String nextStr = request.getParameter("next");
		String prevStr = request.getParameter("prev");

		// プレイリスト再生モード
		String playlistModeStr = request.getParameter("playlistMode");
		String posStr = request.getParameter("pos");
		String actionStr = request.getParameter("action"); // next / prev

		System.out.println("idStr: " + idStr + ", urlStr: " + urlStr + ", nextStr: " + nextStr + ", prevStr: " + prevStr
				+ ", playlistMode: " + playlistModeStr + ", pos: " + posStr + ", action: " + actionStr);

		if (idStr == null && urlStr == null && nextStr == null && prevStr == null
				&& playlistModeStr == null && posStr == null && actionStr == null) {
			// ログインチェック
			HttpSession session = request.getSession();
			String userName = (String) session.getAttribute("user_name");
			System.out.println("userName:" + userName);
			if (userName == null) {
				response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
				return;
			}
			// 曲一覧を取得して JSP へ
			List<Music> musicList = service.getMusicList();
			// セッションスコープに保存
			session.setAttribute("musicList", musicList);
			System.out.println("DAOからとってきた曲リスト" + musicList);
			// フォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/top.jsp");
			dispatcher.forward(request, response);
			System.out.println("曲がないのでtopに戻ります");

		} else {
			// ログインチェック
			HttpSession session = request.getSession();
			String userName = (String) session.getAttribute("user_name");
			if (userName == null) {
				response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
				return;
			}
			
			Music music = null;

			boolean isPlaylistMode = "true".equals(playlistModeStr);
			System.out.println("isPlaylistMode:" + isPlaylistMode);

			// プレイリスト再生モード
			if (isPlaylistMode) {
				User user = new User(userName, "");
				PlaylistDAO playlistDAO = new PlaylistDAO();
				int size = playlistDAO.getPlaylistSize(user);
				if (size <= 0) {
					response.sendRedirect(request.getContextPath() + "/MyPlaylist");
					return;
				}

				Integer currentPos = (Integer) session.getAttribute("currentPlaylistPos");
				if (currentPos == null) currentPos = 0;

				// 直接位置指定（一覧から再生/URL共有など）
				if (posStr != null && !posStr.isEmpty()) {
					currentPos = Integer.parseInt(posStr);
				}

				// action=next/prev の場合はセッションの現在位置を基準に進める
				if ("next".equals(actionStr)) {
					currentPos = currentPos + 1;
				} else if ("prev".equals(actionStr)) {
					currentPos = currentPos - 1;
				}

				// 範囲を循環
				int safePos = (currentPos % size + size) % size;
				session.setAttribute("currentPlaylistPos", safePos);

				music = playlistDAO.getMusicByPos(user, safePos);
				if (music == null) {
					response.sendRedirect(request.getContextPath() + "/MyPlaylist");
					return;
				}

				request.setAttribute("isPlaylistMode", true);
				request.setAttribute("playlistPos", safePos);
				request.setAttribute("playlistSize", size);
			} else {
				// 次の曲ボタンが押された場合（ID順で次の曲を取得）
				if (nextStr != null && !nextStr.isEmpty()) {
					int currentId = Integer.parseInt(nextStr);
					music = service.getNextMusicById(currentId);
				}
				// 前の曲ボタンが押された場合（ID順で前の曲を取得）
				else if (prevStr != null && !prevStr.isEmpty()) {
					int currentId = Integer.parseInt(prevStr);
					music = service.getPrevMusicById(currentId);
				}
				// URLパラメータが指定された場合
				else if (urlStr != null && !urlStr.isEmpty()) {
					music = service.getMusicByUrl(urlStr);
				}
				// IDパラメータが指定された場合（従来通り）
				else if (idStr != null && !idStr.isEmpty()) {
					int id = Integer.parseInt(idStr);
					music = service.getMusic(id);
				}

				// ID順のリストをセッションに保存（次の曲ボタン用）
				List<Music> musicListByIdOrder = service.getMusicListByIdOrder();
				session.setAttribute("musicListByIdOrder", musicListByIdOrder);
				request.setAttribute("isPlaylistMode", false);
			}

			if (music == null) {
				// 曲が見つからない場合はトップに戻る
				response.sendRedirect(request.getContextPath() + "/PlayMusic");
				return;
			}
			request.setAttribute("music", music);

			// プレイリスト登録状態を取得（ボタン表示用）
			User user = new User(userName, "");
			PlaylistDAO playlistDAO = new PlaylistDAO();
			boolean isInPlaylist = playlistDAO.isInPlaylist(user, music.getId());
			request.setAttribute("isInPlaylist", isInPlaylist);

			// フォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/playMusic.jsp");
			dispatcher.forward(request, response);
			System.out.println("曲とばすことはでけた！");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
