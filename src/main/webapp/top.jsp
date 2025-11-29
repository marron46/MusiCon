<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>トップ</title>
<%-- cssの連携 --%>
<link rel="stylesheet" href="css/top.css">
</head>
<body>
	<%-- チェックボックス(非表示) --%>
	<input type="checkbox" id="menu-check" class="menu-check">

	<%-- ハンバーガーアイコン --%>
	<label for="menu-check" class="hamburger">
		<div class="line"></div>
		<div class="line"></div>
		<div class="line"></div>
	</label>
	<!-- 半透明オーバーレイ -->
	<div class="overlay"></div>
	<%-- メニュー --%>
	<nav class="side-menu">
		<ul>
			<li><a href="myBookmark.jsp" class="menu">マイページ</a></li>
			<li><a href="showRanking.jsp" class="menu">ランキング</a></li>
			<li><a href="importMusic.jsp" class="menu">曲追加</a></li>
			<li><a href="login.jsp" class="menu">ログイン</a></li>
			<li><a>ログアウト</a></li>
			<li><a href="deleteUser.jsp" class="menu">アカウント削除</a></li>
		</ul>
	</nav>

	<!-- ▼ オーバーレイクリックで閉じるスクリプト -->
	<script>
    document.querySelector(".overlay").addEventListener("click", () => {
        document.getElementById("menu-check").checked = false;
    });
</script>
	<div class="main">
		<form action="Seach" method="post">
			<%-- 検索バー --%>
			<div class="search-area">
				<img src="searchMark.png" class="search" width="70"> <input type="text"
					class="searchbox" placeholder="検索">
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
</body>
</html>