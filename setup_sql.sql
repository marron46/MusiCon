-- MusiCon 初期セットアップSQL（プレイリスト版）
-- MySQL を想定

CREATE DATABASE IF NOT EXISTS musicon
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;

USE musicon;

-- =========================
-- MUSICS
-- =========================
CREATE TABLE IF NOT EXISTS musics (
  id INT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  genre VARCHAR(15) NOT NULL,
  artist VARCHAR(50) NOT NULL,
  lyricist VARCHAR(50),
  composer VARCHAR(50),
  release_ymd INT,
  music_time INT,
  likes INT DEFAULT 0,
  url VARCHAR(500) NOT NULL,
  uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_musics_title ON musics(title);
CREATE INDEX idx_musics_artist ON musics(artist);

-- =========================
-- USERS
-- =========================
CREATE TABLE IF NOT EXISTS users (
  user_id INT PRIMARY KEY AUTO_INCREMENT,
  user_name VARCHAR(20) NOT NULL,
  user_pass VARCHAR(255) NOT NULL,
  is_deleted BOOLEAN DEFAULT FALSE,
  UNIQUE KEY uq_users_user_name (user_name)
);

-- =========================
-- PLAYLIST_ITEMS（ユーザーごとの順序付きプレイリスト）
-- =========================
CREATE TABLE IF NOT EXISTS playlist_items (
  playlist_item_id INT AUTO_INCREMENT PRIMARY KEY,
  p_user INT NOT NULL,
  p_music INT NOT NULL,
  p_pos INT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_playlist_user_music (p_user, p_music),
  KEY idx_playlist_user_pos (p_user, p_pos),
  CONSTRAINT fk_playlist_user FOREIGN KEY (p_user) REFERENCES users(user_id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_playlist_music FOREIGN KEY (p_music) REFERENCES musics(id)
    ON DELETE CASCADE ON UPDATE CASCADE
);

-- =========================
-- 注意
-- =========================
-- ブックマーク機能は廃止したため bookmarks テーブルは作りません。
