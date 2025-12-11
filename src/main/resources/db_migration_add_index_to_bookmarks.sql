-- BOOKMARKテーブルにB_INDEXカラムを追加するマイグレーションスクリプト
-- このスクリプトを実行して、ブックマークテーブルにINDEXカラムを追加してください

-- 1. B_INDEXカラムを追加（NULL許可、後で既存データにINDEXを設定）
ALTER TABLE BOOKMARKS ADD COLUMN B_INDEX INT NULL;

-- 2. 既存のデータに対して、各ユーザーごとにINDEXを設定
-- ユーザーごとにBOOKMARK_ID順でINDEXを割り当てる
UPDATE BOOKMARKS b1
SET B_INDEX = (
    SELECT COUNT(*)
    FROM BOOKMARKS b2
    WHERE b2.B_USER = b1.B_USER
    AND b2.BOOKMARK_ID <= b1.BOOKMARK_ID
) - 1;

-- 3. B_INDEXをNOT NULLに変更（既存データにINDEXが設定された後）
ALTER TABLE BOOKMARKS MODIFY COLUMN B_INDEX INT NOT NULL;

-- 4. インデックスを追加（パフォーマンス向上のため）
CREATE INDEX idx_bookmarks_user_index ON BOOKMARKS(B_USER, B_INDEX);

-- 確認用クエリ
-- SELECT B_USER, B_MUSIC, B_INDEX FROM BOOKMARKS ORDER BY B_USER, B_INDEX;
