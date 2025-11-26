<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ブックマーク</title>
<%-- cssの連携 --%>
<link rel="stylesheet"
	href="webapp/css/style.css">
</head>
<body>
	<a href="top.jsp" class="top">TOP</a>
	<h1>ブックマーク</h1>
	<%-- ページの見出し部分 --%>
	<ul class="bookMark-list">
			<c:choose>
				<c:when test="${not empty 無記入}">
					<c:forEach var="s" items="${無記入}" varStatus="st">
						<li class="bookmark-item"><c:out value="${s.title}" />-<c:out
								value="${s.artist}" /></li>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<li class="bookmark-item">無記入</li>
					<li class="bookmark-item">無記入</li>
					<li class="bookmark-item">無記入</li>
					<li class="bookmark-item">無記入</li>
					<li class="bookmark-item">無記入</li>
					<li class="bookmark-item">無記入</li>
					<li class="bookmark-item">無記入</li>
				</c:otherwise>
			</c:choose>
		</ul>
</body>
</html>