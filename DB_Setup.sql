-- MusiCon データベース作成スクリプト
-- データベース名: musicon
-- 文字コード: UTF-8

-- データベース作成（存在しない場合）
CREATE DATABASE IF NOT EXISTS musicon CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- データベースを使用
USE musicon;

-- ============================================
-- USERS テーブル（ユーザー情報）
-- ============================================
CREATE TABLE IF NOT EXISTS USERS (
    USER_ID INT AUTO_INCREMENT COMMENT 'ユーザーID',
    USER_NAME VARCHAR(100) NOT NULL COMMENT 'ユーザー名',
    USER_PASS VARCHAR(255) NOT NULL COMMENT 'パスワード（ハッシュ化済み）',
    IS_DELETED TINYINT(1) NOT NULL DEFAULT 0 COMMENT '削除フラグ（0:有効, 1:削除済み）',
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    PRIMARY KEY (USER_ID),
    UNIQUE KEY uk_user_name (USER_NAME),
    INDEX idx_is_deleted (IS_DELETED)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ユーザー情報テーブル';

-- ============================================
-- MUSICS テーブル（音楽情報）
-- ============================================
CREATE TABLE IF NOT EXISTS MUSICS (
    ID INT AUTO_INCREMENT COMMENT '音楽ID',
    TITLE VARCHAR(255) NOT NULL COMMENT '曲名',
    ARTIST VARCHAR(255) COMMENT 'アーティスト名',
    GENRE VARCHAR(100) COMMENT 'ジャンル',
    LYRICIST VARCHAR(255) COMMENT '作詞者',
    COMPOSER VARCHAR(255) COMMENT '作曲者',
    RELEASE_YMD INT COMMENT 'リリース日（YYYYMMDD形式）',
    MUSIC_TIME INT COMMENT '再生時間（秒）',
    URL VARCHAR(500) COMMENT '音楽ファイルのURL',
    LIKES INT NOT NULL DEFAULT 0 COMMENT 'いいね数',
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    PRIMARY KEY (ID),
    INDEX idx_title (TITLE),
    INDEX idx_artist (ARTIST),
    INDEX idx_likes (LIKES)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='音楽情報テーブル';

-- ============================================
-- BOOKMARKS テーブル（ブックマーク情報）
-- ============================================
CREATE TABLE IF NOT EXISTS BOOKMARKS (
    BOOKMARK_ID INT AUTO_INCREMENT COMMENT 'ブックマークID',
    B_USER INT NOT NULL COMMENT 'ユーザーID（USERS.USER_IDへの外部キー）',
    B_MUSIC INT NOT NULL COMMENT '音楽ID（MUSICS.IDへの外部キー）',
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
    PRIMARY KEY (BOOKMARK_ID),
    UNIQUE KEY uk_user_music (B_USER, B_MUSIC),
    FOREIGN KEY (B_USER) REFERENCES USERS(USER_ID) ON DELETE CASCADE,
    FOREIGN KEY (B_MUSIC) REFERENCES MUSICS(ID) ON DELETE CASCADE,
    INDEX idx_b_user (B_USER),
    INDEX idx_b_music (B_MUSIC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ブックマーク情報テーブル';

-- ============================================
-- サンプルデータ（オプション）
-- ============================================

-- サンプルユーザー（パスワードは "password" をSHA-256でハッシュ化したもの）
-- 実際のパスワード: password
-- INSERT INTO USERS (USER_NAME, USER_PASS) VALUES 
-- ('testuser', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8');

-- サンプル音楽データ
-- INSERT INTO MUSICS (TITLE, ARTIST, GENRE, URL, LIKES) VALUES
-- ('サンプル曲1', 'サンプルアーティスト1', 'J-POP', 'http://example.com/music1.mp3', 10),
-- ('サンプル曲2', 'サンプルアーティスト2', 'Rock', 'http://example.com/music2.mp3', 5);

