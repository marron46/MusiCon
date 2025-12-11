package model;



public class Bookmark {
	private int bookmark_id;//	ブックマークID
	private String title ;//タイトル（外部キー）
	private String artist;//アーティスト名（外部キー）
	private int music_id;//楽曲ID
	private int user_id;//ユーザーID
	private int index;//ブックマーク内の順序（INDEX）
	private String music_url;//楽曲URL
	
	private Music music;
	
    // コンストラクタ : フィールド設定
	public Bookmark() {}
	public Bookmark(int bookmark_id,String title,String artist,int music_id){
		this.bookmark_id = bookmark_id;
		this.title = title;
		this.artist = artist;
		this.music_id = music_id;
	}
	
	public Bookmark(int bookmark_id, String title, String artist, int music_id, int index, String music_url) {
		this.bookmark_id = bookmark_id;
		this.title = title;
		this.artist = artist;
		this.music_id = music_id;
		this.index = index;
		this.music_url = music_url;
	}
	
	public Bookmark(int user_id, int music_id) {
		this.user_id = user_id;
		this.music_id = music_id;
	}
	
	//　フィールドの中身をそれぞれ取得するゲッター
	public int getBookmark_id() { return bookmark_id; }
	public String getTitle() { return title; }
	public String getArtist() { return artist; }
	public int getMusic_id() { return music_id; }
	public int getIndex() { return index; }
	public String getMusic_url() { return music_url; }
	
	public Music getMusic() { return music; }
	
	// セッター
	public void setIndex(int index) { this.index = index; }
	public void setMusic_url(String music_url) { this.music_url = music_url; }
}
