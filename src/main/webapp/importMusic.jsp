<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>曲の追加</title>
<%-- cssの連携 --%>
<link rel="stylesheet"
	href="webapp/css/style.css">
</head>
<body>
	<div class="container">
	<a href="top.jsp" class="top">TOP</a>
		<%-- ページの見出し部分 --%>
		<h1 class="cUser-title">曲の追加</h1>

		<form action="ImportMusic" method="post">
			<%-- 曲名の入力欄 --%>
			<label>曲名</label> <input type="text" name="title" required>

			<%-- アーティストの入力欄 --%>
			<label>アーティスト</label><input type="text" name="artist"
				required>
				
			<%-- ジャンルの入力欄 --%>
			<label>ジャンル</label><input type="text" name="genre"
				required>
				
			<%-- リリース日の入力欄 --%>
			<label>リリース日</label><input type="text" name="releaseYMD"
				required>
				
			<%-- 作詞者の入力欄 --%>
			<label>作詞者</label><input type="text" name="lyricist"
				required>
				
			<%-- 作曲者の入力欄 --%>
			<label>作曲者</label><input type="text" name="composer"
				required>
				
			<%-- 再生時間の入力欄 --%>
			<label>再生時間</label><input type="text" name="music_time"
				required>
				
			<%-- 追加ボタン --%>
			<button type="submit">追加</button>
		</form>
	</div>
</body>
</html>