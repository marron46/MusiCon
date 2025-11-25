package model;

import java.sql.Time;

public class Music {//Music情報格納
	private String title;//タイトル
	private String genre;//ジャンル
	private String artist;//アーティスト名
	private String lyricist;//作詞家
	private String composer;//作曲家
	private int releaseYear;//発売年
	private Time music_time;//再生時間
	private int like;//いいね数
	
	// コンストラクタ : フィールド設定
	public Music(String title,String genre,String artist,String lyricist,String composer,int releaseYear,Time music_time,int like) {
		this.title = title;
		this.genre = genre;
		this.artist = artist;
		this.lyricist = lyricist;
		this.composer = composer;
		this.releaseYear = releaseYear;
		this.music_time = music_time;
		this.like = like;
	}
	
	public String getTitle() { return title; }
	public String getGenre() { return genre;}
	public String getArtist() { return artist; }
	public String getLyricist() { return lyricist; }
	public String getComposer() { return composer; }
	public int getReleaseYear() { return releaseYear; }
	public Time getMusicTime() { return music_time;}
	public int getLike() { return like; }
	

}
