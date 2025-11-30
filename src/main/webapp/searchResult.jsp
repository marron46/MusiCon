<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.Music"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>検索結果</title>

<%-- cssの連携 --%>
<link rel="stylesheet" href="css/style.css">
</head>
<body>
	<%-- ページタイトル --%>
	<div class="top-area">
		<a href="top.jsp" class="top">TOP</a>
	</div>

	<h1>検索結果</h1>

	<%
	List<Music> list = (List<Music>) session.getAttribute("searchList");

	if (list == null || list.isEmpty()) {
	%>
	<p>検索結果がありません。</p>
	<%
	} else {
	%>
	<ul>
		<%
		for (Music m : list) {
		%>
		<li>タイトル：<a href="PlayMusic?id=<%=m.getId()%>"> <%=m.getTitle()%></a><br>
			アーティスト：<%=m.getArtist()%><br> いいね：<%=m.getLikes()%>
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