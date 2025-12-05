<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Music"%>
<%@ page import="model.User"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ブックマークの再生</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/playMusic.css">
<script>
    const params = new URLSearchParams(window.location.search);
    const id = params.get("id");

    document.getElementById("next").onclick = () => {
        window.location.href = `PlayMusic?id=${id}&mode=next`;
    };

    document.getElementById("prev").onclick = () => {
        window.location.href = `PlayMusic?id=${id}&mode=prev`;
    };

    const audio = document.getElementById("audio");
    audio.play();
</script>

</head>

<body>

	<a href="${pageContext.request.contextPath}/PlayMusic" class="back-btn">←
		topに戻る</a>

	<%
	Bookmark music = (Bookmark) request.getAttribute("Bookmarkmusic");
%>

	<!-- ▼ 追加：中央配置用のラッパー ▼ -->
	<div class="center-wrapper">
		<div class="player">

			<!-- 左のジャケット（白無地） -->
			<div class="album-art"></div>

			<!-- 右側情報 -->
			<div class="info">

				<!-- 曲タイトル -->
				<h1><%=music.getTitle()%></h1>

				<!-- アーティスト名 -->
				<p class="artist"><%=music.getArtist()%></p>

				<!-- 再生する audio -->
				<audio id="audio">
					<source src="<%=music.getUrl()%>" type="audio/mpeg">
				</audio>

				<div class="center-block">
					<!-- 再生ボタン -->
					<div class="controls">
						<button id="prev">⏮</button>
						<button id="play" class="play">▶</button>
						<button id="next">⏭</button>
					</div>

					<!-- シークバー -->
					<div class="progress-area">
						<span id="current">0:00</span> <input type="range" id="progress"
							min="0" value="0"> <span id="duration">0:00</span>
					</div>
				</div>

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
					<button type="submit" class="like-btn">ブックマーク</button>
				</form>

				<a
					href="${pageContext.request.contextPath}/MyBookmark?id=<%=music.getId()%>">⭐ブックマーク</a>

			</div>

		</div>

	</div>

</body>
</html>