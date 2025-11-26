<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>検索結果</title>

<%-- cssの連携 --%>
<link rel="stylesheet"
	href="webapp/css/style.css">
</head>
<body>
	<%-- ページタイトル --%>
	<div class="top-area">
		<a href="top.jsp" class="top">TOP</a>
	</div>

		<%-- ページの見出し部分 --%>
		<div class="result-title">"無記入"の検索結果</div>

		<%-- 検索結果一覧 --%>
		<ul class="result-list">
			<c:choose>
				<c:when test="${not empty 無記入}">
					<c:forEach var="s" items="${無記入}" varStatus="st">
						<li class="result-item"><c:out value="${s.title}" />-<c:out
								value="${s.artist}" /></li>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<li class="result-item">無記入</li>
					<li class="result-item">無記入</li>
					<li class="result-item">無記入</li>
					<li class="result-item">無記入</li>
					<li class="result-item">無記入</li>
					<li class="result-item">無記入</li>
					<li class="result-item">無記入</li>
				</c:otherwise>
			</c:choose>
		</ul>
</body>
</html>