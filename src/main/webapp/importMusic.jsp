<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>曲の追加</title>
<%-- cssの連携 --%>
<link rel="stylesheet" href="webapp/css/style.css">
</head>
<body>
	<h1>音楽アップロード</h1>

	<!-- enctype="multipart/form-data" が重要。これを付けないとファイルがサーバーに送れない。 -->
	<form action="ImportMusic" method="post"
		enctype="multipart/form-data">

		<!-- タイトル入力欄（DBのtitleに入る） -->
		タイトル：<br> <input type="text" name="title" required><br>
		<br>

		<!-- 音楽ファイル選択欄（mp3などの音声ファイル） -->
		音楽ファイル：<br>
		<!-- accept="audio/*" は音声ファイルだけ選べるようにする -->
		<input type="file" name="file" accept="audio/*" required><br>
		<br>

		<!-- 送信ボタン。押すとサーブレットにデータが送られる -->
		<input type="submit" value="アップロード">
	</form>

	<br>

	<!-- topページへ戻るリンク -->
	<a href="PlayMusic">← top画面に戻る</a>
</body>
</html>