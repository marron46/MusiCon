package model;

public class Music {//Music情報格納
	private int id;//曲ID
	private String title;//タイトル
	private String genre;//ジャンル
	private String artist;//アーティスト名
	private String lyricist;//作詞家
	private String composer;//作曲家
	private int releaseYMD;//発売年月日
	private int music_time;//再生時間
	private int like;//いいね数
	private String searchText;//検索情報
	private String url;//音楽ファイルURL

	// コンストラクタ : フィールド設定
	public Music() {}

	public Music(String title, String genre, String artist, String lyricist, String composer, int releaseYMD,
			int music_time, String url) {
		this.title = title;
		this.genre = genre;
		this.artist = artist;
		this.lyricist = lyricist;
		this.composer = composer;
		this.releaseYMD = releaseYMD;
		this.music_time = music_time;
		this.url = url;

	}

	public Music(String searchText) {
		this.searchText = searchText;
	}

	public Music(String title, String artist, int likes) {
		this.title = title;
		this.artist = artist;
		this.like = likes;
	}
	
	public Music(int id, String title, int like, String url) { // 曲再生用
		this.id = id;
		this.title = title;
		this.like = like;
		this.url = url;
	}

	public int getId() { return id; }

	public String getTitle() { return title; }

	public String getGenre() { return genre; }

	public String getArtist() { return artist; }

	public String getLyricist() { return lyricist; }

	public String getComposer() { return composer; }

	public int getReleaseYear() { return releaseYMD; }

	public int getMusicTime() { return music_time; }

	public int getLike() { return like; }

	public String getSearchText() { return searchText; }

	public String getUrl() { return url; }

}
