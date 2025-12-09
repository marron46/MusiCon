<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>曲の追加</title>
<%-- cssの連携 --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/importMusic.css">
</head>
<body>
	<div class="reverse">
		<a href="${pageContext.request.contextPath}/PlayMusic"> 
		<img src="${pageContext.request.contextPath}/png/MusiConLogo.png"
			alt="TOPに戻る" class="reverse-img">
		</a>
	</div>
	<div class="container">
		<h1 class="iMusic-title">音楽アップロード</h1>
		<!-- enctype="multipart/form-data" が重要。これを付けないとファイルがサーバーに送れない。 -->
		<form action="${pageContext.request.contextPath}/ImportMusic"
			method="post" enctype="multipart/form-data" class="grid">
			<div class="form-item">
				<!-- タイトル入力欄（DBのtitleに入る） -->
				<!--タイトル：-->
				<input type="text" name="title" placeholder="タイトル" required>
			</div>

			<!-- アーティスト入力欄（DBのartistに入る） -->
			<!--アーティスト名：-->
			<div class="form-item">
				<input type="text" name="artist" placeholder="アーティスト" required>
			</div>
			<!-- ジャンル入力欄（DBのgenreに入る） -->
			<!--ジャンル：-->
			<div class="form-item">
				<input type="text" name="genre" placeholder="ジャンル" required>
			</div>

			<!-- 作詞家入力欄（DBのlyricistに入る） -->
			<!--作詞家：-->
			<div class="form-item">
				<input type="text" name="lyricist" placeholder="作詞者" required>
			</div>

			<!-- 作曲家入力欄（DBのcomposerに入る） -->
			<!--作曲家：-->
			<div class="form-item">
				<input type="text" name="composer" placeholder="作曲者" required>
			</div>

			<!-- 発売年月日入力欄（DBのrelease_ymdに入る） -->
			<!--発売年月日：-->
			<div class="form-item">
				<input type="text" name="releaseYMD" placeholder="発売年月日（半角8桁：YYYYMMDD）" required>
			</div>

			<!-- 再生時間入力欄（DBのmusic_timeに入る） -->
			<!--再生時間：-->
			<div class="form-item">
				<input type="text" name="music_time" placeholder="再生時間（半角4桁：mmss）" required>
			</div>

			<!-- 音楽ファイル選択欄（mp3などの音声ファイル） -->
			<div class="form-item">
				<!-- accept="audio/*" は音声ファイルだけ選べるようにする -->
				<input type="file" name="file" accept="audio/*" required>
			</div>
			<!-- 送信ボタン。押すとサーブレットにデータが送られる -->
			<div class="form-item"
				style="grid-column: span 2; text-align: center;">
				<input type="submit" value="アップロード" class="submit-btn">
			</div>
		</form>
	</div>
</body>
</html>