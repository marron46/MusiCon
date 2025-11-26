package model.logic;

import dao.MusicDAO;

public class PlayMusicLogic {
	//曲を再生する
	public boolean execute(Music music) {
		//インスタンス生成
		MusicDAO dao = new MusicDAO();
		if(dao./*メソッド名*/(music) {
			//再生成功
			return true;
		}
		//失敗
		return false;
	}
}
