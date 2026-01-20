<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>曲のアップロード</title>
<%-- cssの連携 --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/importMusic.css">
</head>
<body>
	<div class="reverse">
		<a href="${pageContext.request.contextPath}/PlayMusic"> <img
			src="${pageContext.request.contextPath}/png/MusiConLogo.png"
			alt="TOPに戻る" class="reverse-img">
		</a>
	</div>
	<div class="container">
		<h1 class="iMusic-title">曲アップロード</h1>
		<!-- enctype="multipart/form-data" が重要。これを付けないとファイルがサーバーに送れない。 -->
		<form action="${pageContext.request.contextPath}/ImportMusic"
			method="post" enctype="multipart/form-data" class="grid">
			<div class="form-item">
				<!-- タイトル入力欄（DBのtitleに入る） -->
				<!--タイトル：-->
				<input type="text" name="title" placeholder="タイトル（必須）" required>
			</div>

			<!-- アーティスト入力欄（DBのartistに入る） -->
			<!--アーティスト名：-->
			<div class="form-item">
				<input type="text" name="artist" placeholder="アーティスト（必須）" required>
			</div>
			<!-- 作詞家入力欄（DBのlyricistに入る） -->
			<!--作詞家：-->
			<div class="form-item">
				<input type="text" name="lyricist" placeholder="作詞者（必須）" required>
			</div>

			<!-- 作曲家入力欄（DBのcomposerに入る） -->
			<!--作曲家：-->
			<div class="form-item">
				<input type="text" name="composer" placeholder="作曲者（必須）" required>
			</div>

			<!-- 発売年月日入力欄（DBのrelease_ymdに入る） -->
			<!--発売年月日：-->
			<div class="form-item">
				<input type="text" name="releaseYMD"
					placeholder="発売年月日（半角8桁：YYYYMMDD）">
			</div>
			
			<!-- ジャンル入力欄（DBのgenreに入る） -->
			<!--ジャンル：-->
			<div class="form-item pull-down">
				<select name="genre" required>
					<option class="" disabled selected>ジャンルを選択（必須）</option>
					<option value="JPOP">J-POP</option>
					<option value="KPOP">K-POP</option>
					<option value="HPOP">HIPHOP</option>
					<option value="CLSC">クラシック</option>
					<option value="ROCK">ロック</option>
					<option value="JAZZ">ジャズ</option>
					<option value="RYBL">R&B</option>
					<option value="EDMS">EDM</option>
					<option value="ENKA">演歌</option>
					<option value="OTHR">その他</option>
				</select>
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
			<!--26/01/20 再生時間をmetadataで取る作業の途中-->
			<div class="form-item">
				<input type="text" name="music_time" placeholder="1234" required>
			</div>
		</form>
	</div>
</body>
</html>