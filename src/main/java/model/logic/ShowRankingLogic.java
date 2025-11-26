package model.logic;

import java.util.List;

import dao.MusicDAO;
import model.Music;

public class ShowRankingLogic {//ランキングの情報を取得
	private MusicDAO dao = new MusicDAO();

	public List<Music> getRanking() {
		// DAO でランキング全件取得
		List<Music> allList = dao.getRanking();

		// --- ここで TOP10 に制限 ---
		if (allList.size() > 10) {
			return allList.subList(0, 10); // リストの10件目までを返す
		}
		return allList;
	}
}
