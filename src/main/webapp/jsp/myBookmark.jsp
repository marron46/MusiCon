<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Bookmark"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ブックマーク</title>
<%-- cssの連携 --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/myBookmark.css">
</head>
<body>
	<div class="reverse">
		<a href="${pageContext.request.contextPath}/PlayMusic" class="top">TOPへ戻る</a>
	</div>

	<h2>📌 ブックマーク一覧</h2>

	<p style="color: green;">
		<%=request.getAttribute("message") != null ? request.getAttribute("message") : ""%>
	</p>

	<%
	List<Bookmark> list = (List<Bookmark>) session.getAttribute("bookmarkList");
	if (list == null || list.isEmpty()) {
	%>
	<p>まだブックマークがありません。</p>
	<%
	} else {
	%>
	<ul>
		<%
		for (Bookmark b : list) {
		%>
		<%
		// URLがnullの場合はIDから取得（後方互換性）
		String bookmarkUrl = b.getMusic_url();
		if (bookmarkUrl == null || bookmarkUrl.isEmpty()) {
			// IDからURLを構築（既存データ用）
			bookmarkUrl = request.getContextPath() + "/music/" + b.getMusic_id();
		}
		%>
		<li>🎵 <a
			href="${pageContext.request.contextPath}/PlayMusic?bookmarkMode=true&url=<%=java.net.URLEncoder.encode(bookmarkUrl, "UTF-8")%>"
			class="music-area btn-flat"> <span><%=b.getTitle()%></span>
		</a> <br> <br>
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