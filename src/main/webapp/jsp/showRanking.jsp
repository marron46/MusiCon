<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Music"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ランキング</title>

<%-- cssの連携 --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/showRanking.css">
</head>
<body>
	<div class="reverse">
		<a href="${pageContext.request.contextPath}/PlayMusic"> 
		<img src="${pageContext.request.contextPath}/png/MusiConLogo.png"
			alt="TOPに戻る" class="reverse-img">
		</a>
	</div>
	<%-- ページタイトル --%>
	<div class="rank-title">人気曲ランキング</div>
	</div>
	<div class="container">

		<%-- ランキング一覧 --%>

		<%
		List<Music> list = (List<Music>) session.getAttribute("ranking");

		if (list == null || list.isEmpty()) {
		%>
		<p>検索結果がありません。</p>
		<%
		} else {
		%>
		<ul>
			<%
			for (model.Music m : list) {
			%>
			<li>タイトル：<a
				href="${pageContext.request.contextPath}/PlayMusic?url=<%=java.net.URLEncoder.encode(m.getUrl(), "UTF-8")%>">
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

		<%--<ul class="rank-list">
			<c:choose>
				<c:when test="${not empty 無記入}">
					<c:forEach var="s" items="${無記入}" varStatus="st">
						<li class="rank-item"><c:out value="${s.title}" />-<c:out
								value="${s.artist}" /></li>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<li class="rank-item"></li>
					<li class="rank-item">無記入</li>
					<li class="rank-item">無記入</li>
					<li class="rank-item">無記入</li>
					<li class="rank-item">無記入</li>
					<li class="rank-item">無記入</li>
					<li class="rank-item">無記入</li>
				</c:otherwise>
			</c:choose>
		</ul>--%>

		<div class="main"></div>

	</div>
	</div>
</body>
</html>