package model.logic;

import dao.MusicDAO;
import model.Music;

public class ImportMusicLogic {
	
	//曲を追加
		public boolean execute (Music music) {

			//DAOインスタンスを生成
			MusicDAO dao = new MusicDAO();
			
			//DAOメソッド呼び出し・追加実行
			if(dao.importMusic(music)) { 
				// 追加成功
				return true;
			}		
			// 追加失敗
			return false;
		}
}
