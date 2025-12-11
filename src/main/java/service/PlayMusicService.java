package service;

import java.util.List;

import dao.MusicDAO;
import model.Music;

/**
 * 音楽再生に関するビジネスロジックを提供するサービスクラス
 */
public class PlayMusicService {
	private MusicDAO dao = new MusicDAO();
	
	/**
	 * トップページに表示する曲リストを取得
	 * @return 音楽リスト
	 */
	public List<Music> getMusicList() {
		return dao.topFindAll();
	}
	
	/**
	 * IDで音楽を取得
	 * @param id 音楽ID
	 * @return 音楽オブジェクト
	 */
	public Music getMusic(int id) {
		return dao.playMusicById(id);
	}
	
	/**
	 * URLで音楽を取得
	 * @param url 音楽URL
	 * @return 音楽オブジェクト
	 */
	public Music getMusicByUrl(String url) {
		return dao.findByUrl(url);
	}
	
	/**
	 * ID順で曲リストを取得
	 * @return ID順の音楽リスト
	 */
	public List<Music> getMusicListByIdOrder() {
		return dao.getMusicListByIdOrder();
	}
	
	/**
	 * ID順で次の曲を取得
	 * @param currentId 現在の音楽ID
	 * @return 次の音楽オブジェクト
	 */
	public Music getNextMusicById(int currentId) {
		return dao.getNextMusicById(currentId);
	}
	
	/**
	 * ID順で前の曲を取得
	 * @param currentId 現在の音楽ID
	 * @return 前の音楽オブジェクト
	 */
	public Music getPrevMusicById(int currentId) {
		return dao.getPrevMusicById(currentId);
	}
	
	/**
	 * 音楽にいいねを追加
	 * @param id 音楽ID
	 * @return 成功時はtrue、失敗時はfalse
	 */
	public boolean likeMusic(int id) {
		return dao.likeMusic(id);
	}
}
