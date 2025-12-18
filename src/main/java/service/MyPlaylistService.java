package service;

import java.util.List;

import dao.PlaylistDAO;
import model.PlaylistItem;
import model.Music;
import model.User;

/**
 * プレイリストに関するビジネスロジック
 */
public class MyPlaylistService {
	private final PlaylistDAO dao = new PlaylistDAO();

	public List<PlaylistItem> getPlaylist(User user) {
		return dao.getPlaylist(user);
	}

	public boolean isInPlaylist(User user, Music music) {
		if (music == null) return false;
		return dao.isInPlaylist(user, music.getId());
	}

	public boolean toggle(User user, Music music) {
		if (music == null) return false;
		if (dao.isInPlaylist(user, music.getId())) {
			return dao.removeFromPlaylist(user, music.getId());
		}
		return dao.addToPlaylist(user, music.getId());
	}
}


