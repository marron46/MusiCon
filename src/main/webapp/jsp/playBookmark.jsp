<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Music"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ブックマーク再生</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/playMusic.css">
</head>

<body>

	<a href="${pageContext.request.contextPath}/MyBookmark"
		class="back-btn">← ブックマークリストへ戻る</a>

	<%
	Music music = (Music) request.getAttribute("music");
	int index = (int) request.getAttribute("index");
	List<Music> musicList = (List<Music>) request.getAttribute("musicList");
	int total = musicList.size();

	int nextIndex = (index + 1) % total;
	int prevIndex = (index - 1 + total) % total;
	%>

	<div class="center-wrapper">
		<div class="player">

			<div class="album-art"></div>

			<div class="info">
				<h1><%=music.getTitle()%></h1>
				<p class="artist"><%=music.getArtist()%></p>

				<audio id="audio" autoplay>
					<source src="<%=music.getUrl()%>" type="audio/mpeg">
				</audio>

				<div class="center-block">
					<div class="controls">
						<button id="prev">⏮</button>
						<button id="play" class="play">▶</button>
						<button id="next">⏭</button>
					</div>

					<div class="progress-area">
						<span id="current">0:00</span> <input type="range" id="progress"
							min="0" value="0"> <span id="duration">0:00</span>
					</div>
				</div>

				<form action="${pageContext.request.contextPath}/LikeMusic"
					method="post">
					<input type="hidden" name="id" value="<%=music.getId()%>">
					<button type="submit" class="like-btn">
						いいね！(<%=music.getLikes()%>)
					</button>
				</form>

				<form action="${pageContext.request.contextPath}/MyBookmark"
					method="post">
					<input type="hidden" name="id" value="<%=music.getId()%>">
					<button type="submit" class="like-btn">⭐ブックマーク</button>
				</form>

				<form action="${pageContext.request.contextPath}BookmarkPlay"
					method="post">
					<input type="hidden" name="id" value="<%=music.getId()%>">
					<input type="hidden" name="index" value="<%=index%>">

					<%
					boolean bookmarked = (Boolean) request.getAttribute("isBookmarked");
					%>

					<button type="submit" class="like-btn"
						style="background:<%=bookmarked ? "#f7d358" : "#dddddd"%>">
						<%=bookmarked ? "★ ブックマーク解除" : "☆ ブックマーク"%>
					</button>
				</form>

			</div>
		</div>
	</div>

	<script>
const audio = document.getElementById("audio");

document.getElementById("play").onclick = () => {
    if (audio.paused) audio.play();
    else audio.pause();
};

document.getElementById("next").onclick = () => {
    window.location.href = "BookmarkPlay?index=<%=nextIndex%>";
};

document.getElementById("prev").onclick = () => {
    window.location.href = "BookmarkPlay?index=<%=prevIndex%>";
};

// 自動次曲
audio.onended = () => {
    window.location.href = "BookmarkPlay?index=<%=nextIndex%>";
};
</script>

</body>
</html>