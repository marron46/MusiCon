<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Music"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>楽曲一覧</title>

<%-- cssの連携 --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/musicList.css">
</head>
<body>

	<div class="reverse">
		<a href="${pageContext.request.contextPath}/PlayMusic"> <img
			src="${pageContext.request.contextPath}/png/MusiConLogo.png"
			alt="TOPに戻る" class="reverse-img">
		</a>
	</div>

	<h1 class="page-title">楽曲一覧</h1>

	<%
	List<Music> list = (List<Music>) request.getAttribute("musicList");
	if (list == null || list.isEmpty()) {
	%>
	<p class="empty">曲がありません。</p>
	<%
	} else {
	%>
	<div class="container">
		<%
		for (Music m : list) {
			String playLink;
			if (m.getUrl() != null && !m.getUrl().isEmpty()) {
				playLink = pageContext.getRequest().getServletContext().getContextPath()
				+ "/PlayMusic?url=" + java.net.URLEncoder.encode(m.getUrl(), "UTF-8");
			} else {
				playLink = pageContext.getRequest().getServletContext().getContextPath()
				+ "/PlayMusic?id=" + m.getId();
			}
		%>

		<a href="<%=playLink%>" class="music-area btn-flat">
			<div class="title">
				タイトル：<%=m.getTitle()%></div>
			<div class="artist">
				アーティスト：<%=m.getArtist()%></div>
			<div class="time">
				再生時間：<%=m.getMusicTime() / 100%>:<%=String.format("%02d", m.getMusicTime() % 100)%></div>
			<div class="like">
				いいね：<%=m.getLikes()%></div>
		</a>

		<%
		}
		%>
	</div>
	<%
	}
	%>

</body>
</html>