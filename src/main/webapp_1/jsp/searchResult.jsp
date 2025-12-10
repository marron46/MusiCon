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
	<div class="reverse">
		<a href="${pageContext.request.contextPath}/PlayMusic"> <img
			src="${pageContext.request.contextPath}/png/MusiConLogo.png"
			alt="TOPに戻る" class="reverse-img">
		</a>
	</div>
	<%
	String schWd = (String) session.getAttribute("searchText");
	System.out.println(schWd);
	%>
	<h1 class="sResult-title">検索結果</h1>

	<%
    List<Music> list = (List<Music>) session.getAttribute("searchList");
%>
	<div class="result-count">
    <% if (list == null || list.isEmpty() || schWd.length() == 0) { %>
        "<%=schWd%>" を含む楽曲：0件
    <% } else { %>
        "<%=schWd%>" を含む楽曲：<%=list.size()%>件
    <% } %>
</div>

<% if (list != null && !list.isEmpty() && schWd.length() != 0) { %>
<div class="container">
    <% for (model.Music m : list) { %>
        <a
            href="${pageContext.request.contextPath}/PlayMusic?id=<%=m.getId()%>"
            class="music-area btn-flat">
            <div class="title">タイトル：<%=m.getTitle()%></div>
            <div class="artist">アーティスト：<%=m.getArtist()%></div>
            <div class="like">いいね：<%=m.getLikes()%></div>
        </a>
    <% } %>
</div>
<% } %>


</body>
</html>