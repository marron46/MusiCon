<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>トップ</title>
<%-- cssの連携 --%>
<link rel="stylesheet" href="webapp/css/style.css">
</head>
<body>


	<%-- ハンバーガーリスト --%>
	<div id="hamburger" class="hamburger">☰</div>

	<%-- サイドバー --%>
	<div id="sidebar" class="sidebar"></div>
	<a href="myBookmark.jsp" class="menu">マイページ</a>
	<a href="showRanking.jsp" class="menu">ランキング</a>
	<a href="importMusic.jsp" class="menu">曲追加</a>
	<a>ログアウト</a>
	<a href="deleteUser.jsp" class="menu">アカウント削除</a>
	</div>

	<div class="main">
		<form action="Seach" method="post">
			<%-- 検索バー --%>
			<divclass"search-area"> <img src="無記入" class="無記入">
			<input type="text" class="searchbox">
	</div>
	</form>

	<%-- 曲リスト --%>
	<ul class="music-list">
		<c:choose>
			<c:when test="${not empty 無記入}">
				<c:forEach var="s" items="${無記入}" varStatus="st">
					<li class="music-item"><c:out value="${s.title}" />-<c:out
							value="${s.artist}" /></li>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<li class="music-item">なんか曲1-なんかアーティスト</li>
				<li class="music-item">なんか曲2-なんかアーティスト</li>
				<li class="music-item">なんか曲3-なんかアーティスト</li>
				<li class="music-item">なんか曲4-なんかアーティスト</li>
				<li class="music-item">なんか曲5-なんかアーティスト</li>
				<li class="music-item">なんか曲6-なんかアーティスト</li>
				<li class="music-item">なんか曲7-なんかアーティスト</li>
			</c:otherwise>
		</c:choose>
	</ul>

	<script>
		//ハンバーガークリックでサイドバー開閉
		document.getElementById("hamburger").onclick = function() {
			document.getElementById("sidebar").classList.toggle("open");
		};
	</script>
</body>
</html>