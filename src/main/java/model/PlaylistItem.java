package model;

/**
 * ユーザーごとのプレイリスト内の1要素（順序付き）
 */
public class PlaylistItem {
	private int playlist_item_id;
	private int user_id;
	private int music_id;
	private int pos; // 0-based の順序
	private Music music;

	public PlaylistItem() {}

	public PlaylistItem(int playlist_item_id, int user_id, int music_id, int pos, Music music) {
		this.playlist_item_id = playlist_item_id;
		this.user_id = user_id;
		this.music_id = music_id;
		this.pos = pos;
		this.music = music;
	}

	public int getPlaylist_item_id() { return playlist_item_id; }
	public int getUser_id() { return user_id; }
	public int getMusic_id() { return music_id; }
	public int getPos() { return pos; }
	public Music getMusic() { return music; }
}


