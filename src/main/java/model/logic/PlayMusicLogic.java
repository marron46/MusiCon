package model.logic;

import java.util.List;

import dao.MusicDAO;
import model.Music;

public class PlayMusicLogic {
	// インスタンス作成
	private MusicDAO dao = new MusicDAO();
	
	// topに表示する曲リスト
	public List<Music> getMusicList() {
        return dao.topFindAll();
    }
	
	// playMusic.jspに送る曲データ
	public Music getMusic(int id) {
		return dao.playMusicById(id);
	}
	
	// いいね処理
	public boolean likeMusic(int id) {
		System.out.println("likeMusicでid取得" + id);
		return dao.likeMusic(id);
	}
}
