package model.logic;

import dao.MusicDAO;

public class ImportMusicLogic {

	//DAOインスタンスを生成
	MusicDAO dao = new MusicDAO();

	//曲を追加
	/*public boolean execute(Music music) {

		//DAOメソッド呼び出し・追加実行
		if (dao.importMusic(music)) {
			// 追加成功
			return true;
		}
		// 追加失敗
		return false;
	}*/

	//
	public void addMusic(String title,String genre,String artist,String lyricist,String composer,int releaseYMD,int music_time,String url) {
		dao.insert(title, genre, artist, lyricist, composer, releaseYMD, music_time, url);
	}
}