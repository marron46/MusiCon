<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.Music"%>
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
		<form action="Search" method="post">
			<%-- 検索バー --%>
			<div class="search-area">
				<img src="png/searchMark.png" class="search" width="70" alt="検索アイコン">
				<input type="text" class="searchbox" name="searchText"
					placeholder="検索">
			</div>
		</form>
		<div class="container">
			<ul>
				<%
				List<model.Music> list = (List<model.Music>) session.getAttribute("musicList");

				// list にデータがあれば1件ずつループ
				for (model.Music m : list) {
				%>

				<!-- 曲タイトルをリンクとして表示 -->
				<!-- クリックすると MusicServlet?id=○○ に飛び、play.jsp で再生画面へ -->

				<li><a href="PlayMusic?id=<%=m.getId()%>" class="music-area">
						<%=m.getTitle()%></a></li>

				<%
				} // for の終わり
				%>
			</ul>
		</div>
</body>
</html>