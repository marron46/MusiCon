package service;

import java.util.List;

import dao.MusicDAO;
import model.Music;

/**
 * ランキング表示に関するビジネスロジックを提供するサービスクラス
 */
public class ShowRankingService {
	private MusicDAO dao = new MusicDAO();

	/**
	 * ランキング情報を取得（TOP10）
	 * @return ランキングの音楽リスト（最大10件）
	 */
	public List<Music> getRanking() {
		List<Music> allList = dao.getRanking();
		if (allList.size() > 10) {
			return allList.subList(0, 10);
		}
		return allList;
	}
}
