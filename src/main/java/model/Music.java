package model;

public class Music {//Music情報格納
	private String title;//タイトル
	private String genre;//ジャンル
	private String artist;//アーティスト名
	private String lyricist;//作詞家
	private String composer;//作曲家
	private int releaseYear;//発売年
	private int music_time;//再生時間
	private int like;//いいね数
	private String searchText;//検索情報
	
	// コンストラクタ : フィールド設定
	public Music() {}
	public Music(String title,String genre,String artist,String lyricist,String composer,int releaseYear,int music_time,int like) {
		this.title = title;
		this.genre = genre;
		this.artist = artist;
		this.lyricist = lyricist;
		this.composer = composer;
		this.releaseYear = releaseYear;
		this.music_time = music_time;
		this.like = like;
	}
	public Music(String searchText) {
		this.searchText = searchText;
	}
	
	public String getTitle() { return title; }
	public String getGenre() { return genre;}
	public String getArtist() { return artist; }
	public String getLyricist() { return lyricist; }
	public String getComposer() { return composer; }
	public int getReleaseYear() { return releaseYear; }
	public int getMusicTime() { return music_time;}
	public int getLike() { return like; }
	public String getsearchText() { return searchText; }
	

}
