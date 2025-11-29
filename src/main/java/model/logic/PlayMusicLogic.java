package model.logic;

import dao.MusicDAO;
import model.Music;

public class PlayMusicLogic {
	// インスタンス作成
	private MusicDAO dao = new MusicDAO();
	
	public Music getMusic(int id) {
		return dao.playMusicById(id);
	}
}
