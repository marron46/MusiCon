<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ランキング</title>

<%-- cssの連携 --%>
<link rel="stylesheet"
	href="webapp/css/style.css">
</head>
<body>
	<%-- ページタイトル --%>
	<div class="header">showRanking</div>
	<div class="main">
		<a href="top.jsp" class="top">TOP</a>
	</div>
	<div class="context">

		<%-- ページの見出し部分 --%>
		<div class="ranking-title">人気曲ランキング</div>

		<%-- ランキング一覧 --%>
		<ul class="rank-list">
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
		</ul>
	</div>
	</div>
</body>
</html>