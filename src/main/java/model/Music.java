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
	private int likes;//いいね数
	private String searchText;//検索情報
	private String url;//音楽ファイルURL

	// コンストラクタ : フィールド設定
	public Music() {
	}

	public Music(String title, String genre, String artist, String lyricist, String composer, int releaseYMD,
			int music_time) {
		this.title = title;
		this.genre = genre;
		this.artist = artist;
		this.lyricist = lyricist;
		this.composer = composer;
		this.releaseYMD = releaseYMD;
		this.music_time = music_time;

	}

	public Music(String searchText) {
		this.searchText = searchText;
	}

	public Music(int id, String title, String artist, int likes, String url) {
		this.id = id;
		this.title = title;
		this.artist = artist;
		this.likes = likes;
		this.url = url;
	}
	
	public Music(int id, String title, String artist, int likes) {
		this.id = id;
		this.title = title;
		this.artist = artist;
		this.likes = likes;
	}

	public Music(int id, String title, int likes, String url) { // 曲再生用
		this.id = id;
		this.title = title;
		this.likes = likes;
		this.url = url;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) { this.id = id; }

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) { this.title = title; }

	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) { this.genre = genre; }

	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) { this.artist = artist; }

	public String getLyricist() {
		return lyricist;
	}
	public void setLyricist(String lyricist) { this.lyricist = lyricist; }

	public String getComposer() {
		return composer;
	}
	public void setComposer(String composer) { this.composer = composer; }

	public int getReleaseYMD() {
		return releaseYMD;
	}
	public void setReleaseYMD(int releaseYMD) { this.releaseYMD = releaseYMD; }

	public int getMusicTime() {
		return music_time;
	}
	public void setMusicTime(int music_time) { this.music_time = music_time; }

	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) { this.likes = likes; }

	public String getSearchText() {
		return searchText;
	}
	public void setSearchText(String searchText) { this.searchText = searchText; }

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) { this.url = url; }

}
