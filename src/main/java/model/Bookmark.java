package model;



public class Bookmark {
	private int bookmark_id;//	ブックマークID
	private String title ;//タイトル（外部キー）
	private String artist;//アーティスト名（外部キー）
	private int music_id;//楽曲ID
	
	private Music music;
	
    // コンストラクタ : フィールド設定
	public Bookmark(int bookmark_id,String title,String artist,int music_id){
		this.bookmark_id = bookmark_id;
		this.title = title;
		this.artist = artist;
		this.music_id = music_id;
	}
	
	//　フィールドの中身をそれぞれ取得するゲッター
	public int getBookmark_id() { return bookmark_id; }
	public String getTitle() { return title; }
	public String getArtist() { return artist; }
	public int getMusic_id() { return music_id; }
	
	public Music getMusic() { return music; }
}
