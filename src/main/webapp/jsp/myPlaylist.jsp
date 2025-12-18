<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.PlaylistItem"%>
<%@ page import="model.Music"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>プレイリスト</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/myPlaylist.css">
</head>
<body>
	<div class="reverse">
		<a href="${pageContext.request.contextPath}/PlayMusic"> <img
			src="${pageContext.request.contextPath}/png/MusiConLogo.png"
			alt="TOPに戻る" class="reverse-img">
		</a>
	</div>

	<h2 class="mBookmark-title">▶ プレイリスト</h2>

	<p style="color: green;">
		<%=request.getAttribute("message") != null ? request.getAttribute("message") : ""%>
	</p>

	<%
	List<PlaylistItem> list = (List<PlaylistItem>) session.getAttribute("playlistItems");
	if (list == null || list.isEmpty()) {
	%>
	<p class="NoBookmark">まだプレイリストが空です。</p>
	<%
	} else {
	%>
	<ul class="container">
		<%
		for (PlaylistItem item : list) {
			Music m = item.getMusic();
		%>
		<li>
			<a href="${pageContext.request.contextPath}/PlayMusic?playlistMode=true&pos=<%=item.getPos()%>"
			   class="music-area btn-flat">
				<div class="title">タイトル：<%=m.getTitle()%></div>
				<div class="artist">アーティスト：<%=m.getArtist()%></div>
				<div class="id">順番：<%=item.getPos() + 1%></div>
			</a>
			<form action="${pageContext.request.contextPath}/MyPlaylist" method="post" style="margin-top: 10px;">
				<input type="hidden" name="id" value="<%=m.getId()%>">
				<button type="submit" class="btn-flat" style="background:#f7d358; border:none; cursor:pointer;">
					★ プレイリストから外す
				</button>
			</form>
			<br><br>
		</li>
		<hr>
		<%
		}
		%>
	</ul>
	<%
	}
	%>
</body>
</html>


