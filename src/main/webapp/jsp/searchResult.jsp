<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.Music"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>検索結果</title>

<%-- cssの連携 --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/searchResult.css">
</head>
<body>
	<%-- ページタイトル --%>
	<div class="reverse">
		<a href="${pageContext.request.contextPath}/PlayMusic"> 
		<img src="${pageContext.request.contextPath}/png/MusiConLogo.png"
			alt="TOPに戻る" class="reverse-img">
		</a>
	</div>
	<%
	String schWd = (String) session.getAttribute("searchText");
	System.out.println(schWd);
	%>
	<h1>検索結果</h1>
	<%
	List<Music> list = (List<Music>) session.getAttribute("searchList");
	
	if (list == null || list.isEmpty() || schWd.length() == 0) {
	%>
	<p>"<%= schWd %>"を含む楽曲：0件</p>
	<%
	} else {
	%>
	<p>"<%= schWd %>"を含む楽曲：<%= list.size() %>件</p>
	<ul>
		<%
		for (model.Music m : list) {
		%>
		<li>タイトル：<a
			href="${pageContext.request.contextPath}/PlayMusic?id=<%=m.getId()%>">
				<%=m.getTitle()%></a><br> アーティスト：<%=m.getArtist()%><br> いいね：<%=m.getLikes()%>
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