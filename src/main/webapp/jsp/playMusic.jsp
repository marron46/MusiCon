<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.Music"%>
<%@ page import="model.User"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>曲の再生</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/playMusic.css">
<script defer src="${pageContext.request.contextPath}/js/playMusic.js"></script>

</head>

<body>

	<%
	// 変数を最初に定義
	Music music = (Music) request.getAttribute("music");
	Boolean isBookmarked = (Boolean) request.getAttribute("isBookmarked");
	Boolean isBookmarkMode = (Boolean) request.getAttribute("isBookmarkMode");
	if (isBookmarked == null) {
		isBookmarked = false;
	}
	if (isBookmarkMode == null) {
		isBookmarkMode = false;
	}
	
	%>

	<div class="reverse">
		<a
			href="<%=isBookmarkMode ? request.getContextPath() + "/MyBookmark" : request.getContextPath() + "/PlayMusic"%>"><img
			src="${pageContext.request.contextPath}/png/MusiConLogo.png"
			alt="TOPに戻る" class="reverse-img"> </a>
	</div>
	<div class="reverseStr">
		<a
			href="<%=isBookmarkMode ? request.getContextPath() + "/MyBookmark" : request.getContextPath() + "/PlayMusic"%>">
			<%=isBookmarkMode ? "ブックマークリストへ戻る" : "TOPに戻る"%>
		</a>
	</div>

	<%
	// musicがnullの場合はエラーページにリダイレクト
		if (music == null) {
			if (isBookmarkMode) {
				response.sendRedirect(request.getContextPath() + "/MyBookmark");
			} else {
				response.sendRedirect(request.getContextPath() + "/PlayMusic");
			}
			return;
		}
		
		// ブックマーク再生モードの場合のindex計算
		int index = 0;
		int nextIndex = 0;
		int prevIndex = 0;
		if (isBookmarkMode) {
			index = (Integer) request.getAttribute("index");
			java.util.List<Music> musicList = (java.util.List<Music>) request.getAttribute("musicList");
			int total = musicList.size();
			nextIndex = (index + 1) % total;
			prevIndex = (index - 1 + total) % total;
		}
	%>

	<%
	String nextUrl;
	String prevUrl;
	if (isBookmarkMode) {
		nextUrl = request.getContextPath() + "/PlayMusic?bookmarkMode=true&bookmarkIndex=" + nextIndex;
		prevUrl = request.getContextPath() + "/PlayMusic?bookmarkMode=true&bookmarkIndex=" + prevIndex;
	} else {
		nextUrl = request.getContextPath() + "/PlayMusic?next=" + music.getId();
		prevUrl = request.getContextPath() + "/PlayMusic?prev=" + music.getId();
	}
	%>
	<script>
	// DOM読み込み後にハンドラを登録（要素未生成によるnull回避＆変数重複回避）
	window.addEventListener("DOMContentLoaded", () => {
		const nextBtn = document.getElementById("next");
		const prevBtn = document.getElementById("prev");
		const audioEl = document.getElementById("audio");
		if (nextBtn) {
			nextBtn.onclick = () => window.location.href = "<%=nextUrl%>";
		}
		if (prevBtn) {
			prevBtn.onclick = () => window.location.href = "<%=prevUrl%>";
		}
		if (audioEl) {
			audioEl.addEventListener("ended", () => {
				window.location.href = "<%=nextUrl%>";
			});
		}
	});
	</script>

	<!-- ▼ 追加：中央配置用のラッパー ▼ -->
	<div id="player-container">
	<!--<div class="center-wrapper">
		<div class="player">-->

			<!-- 左のジャケット（白無地） -->
			<div class="album-art"></div>

			<!-- 右側情報 -->
			<div class="info">

				<!-- 曲タイトル -->
				<h2 class="title"><%=music.getTitle()%></h2>

				<!-- アーティスト名 -->
				<p class="artist"><%=music.getArtist()%></p>

				<!-- 再生する audio -->
				<audio id="audio" preload="metadata" <!-- crossorigin="anonymous" -->>

				<%
					String musicUrl = music.getUrl();
					if (musicUrl != null) {
						// 既にサーブレット経由のURL（MusicFile?file=）の場合はそのまま使用
						if (musicUrl.contains("MusicFile?file=")) {
							// サーブレット経由のURLはそのまま使用
							// コンテキストパスが含まれていない場合は追加
							if (!musicUrl.startsWith("http") && !musicUrl.startsWith(request.getContextPath())) {
								if (musicUrl.startsWith("/")) {
									musicUrl = request.getContextPath() + musicUrl;
								} else {
									musicUrl = request.getContextPath() + "/" + musicUrl;
								}
							}
						}
						// 古い形式のURL（/music/ファイル名.mp3）の場合はサーブレット経由のURLに変換
						// 既存データとの互換性のため、URLエンコードを実行
						else if (musicUrl.contains("/music/")) {
							// ファイル名を抽出
							String fileName = musicUrl.substring(musicUrl.lastIndexOf("/") + 1);
							// サーブレット経由のURLに変換（既存データのためURLエンコード）
							musicUrl = request.getContextPath() + "/MusicFile?file=" + java.net.URLEncoder.encode(fileName, "UTF-8");
						}
						// その他の相対パスの場合
						else if (!musicUrl.startsWith("http") && !musicUrl.startsWith("/")) {
							musicUrl = request.getContextPath() + "/" + musicUrl;
						} else if (musicUrl.startsWith("/") && !musicUrl.startsWith(request.getContextPath())) {
							musicUrl = request.getContextPath() + musicUrl;
						}
					}
					%>
					<source src="<%=musicUrl%>" type="audio/mpeg">
					<p>お使いのブラウザは音声再生に対応していません。</p>
				</audio>

				<div class="center-block">
					<!-- 再生ボタン -->
				<div class="controls">
					<button id="prev">⏮</button>
					<button id="play" class="play">▶</button>
					<button id="next">⏭</button>
				</div>

				<!-- イコライザー -->
				<canvas id="equalizer"></canvas>

				<!-- シークバー -->
					<div class="progress-area">
						<span id="current">0:00</span> <input type="range" id="progress"
							min="0" value="0"> <span id="duration">0:00</span>
					</div>


					<!--  音量バー  -->
					<div class="volume-area">
						<span id="volume-icon">🔊</span> <input type="range" id="volume"
							min="0" max="0.7" step="0.02" value="0.7">
					</div>

				</div>

				<div class="like-bookmark-box">
					<!-- いいね -->
					<form action="${pageContext.request.contextPath}/LikeMusic"
						method="post">
						<input type="hidden" name="id" value="<%=music.getId()%>">
						<button type="submit" class="like-btn">
							いいね！ (<%=music.getLikes()%>)
						</button>
					</form>

					<!-- ブックマーク -->
					<form action="${pageContext.request.contextPath}/MyBookmark"
						method="post">
						<input type="hidden" name="id" value="<%=music.getId()%>">
						<%
					if (isBookmarkMode) {
					%>
						<input type="hidden" name="bookmarkMode" value="true"> <input
							type="hidden" name="bookmarkIndex" value="<%=index%>">
						<%
					}
					%>
						<%
					if (isBookmarked) {
					%>
						<button type="submit" class="like-btn"
							style="background-color: #f7d358;">★ ブックマーク解除</button>
						<%
					} else {
					%>
						<button type="submit" class="like-btn"
							style="background-color: #dddddd;">☆ ブックマーク</button>
						<%
					}
					%>
					</form>
				</div>

			</div>

		</div>

	</div>

</body>
</html>