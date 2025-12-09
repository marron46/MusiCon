package model.logic;

import dao.MusicDAO;

public class ImportMusicLogic {

	//DAOインスタンスを生成
	MusicDAO dao = new MusicDAO();

	//曲を追加
	public void addMusic(String title,String genre,String artist,String lyricist,String composer,int releaseYMD,int music_time,String url) {
		dao.insert(title, genre, artist, lyricist, composer, releaseYMD, music_time, url);
	}
}