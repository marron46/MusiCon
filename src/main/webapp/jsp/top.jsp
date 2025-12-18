<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List, model.Music, service.PlayMusicService"%>
<%
// ログインチェック
String userName = (String) session.getAttribute("user_name");
if (userName == null) {
	response.sendRedirect(request.getContextPath() + "/index.jsp");
	return;
}

// 曲一覧を取得
PlayMusicService service = new PlayMusicService();
List<Music> musicList = service.getMusicList();
session.setAttribute("musicList", musicList);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>トップ</title>
<%-- cssの連携 --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/top.css">
</head>
<body>
<img src="${pageContext.request.contextPath}/png/MusiConLogo.png"
						class="icon" width="180" alt="ロゴアイコン">
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
		<ul class="menu-list">
			<li><a href="${pageContext.request.contextPath}/MyBookmark"
				class="menu">My page / マイページ</a></li>
			<li><a href="${pageContext.request.contextPath}/ShowRanking"
				class="menu">Ranking / ランキング</a></li>
			<li><a
				href="${pageContext.request.contextPath}/jsp/importMusic.jsp"
				class="menu">Add music / 曲追加</a></li>
			<li><a
				href="${pageContext.request.contextPath}/MusicList"
				class="menu">Music list / 楽曲一覧</a></li>
		</ul>
		<ul class="menu-bottom">
			<li><a href="${pageContext.request.contextPath}/Logout"
				class="menu logout">Log out / ログアウト</a></li>
			<li><a
				href="${pageContext.request.contextPath}/jsp/deleteUser.jsp"
				class="menu delete">Delete account / アカウント削除</a></li>
		</ul>
	</nav>

	<!-- ▼ オーバーレイクリックで閉じるスクリプト -->
	<script>
    document.querySelector(".overlay").addEventListener("click", () => {
        document.getElementById("menu-check").checked = false;
    });
</script>
	<div class="main">
		<form action="${pageContext.request.contextPath}/Search" method="post">
			<%-- 検索バー --%>
			<div class="search-area">
				<input type="text" class="searchbox" name="searchText"
					placeholder="キーワードで検索">
				<button type="submit" class="search-button">
					<img src="${pageContext.request.contextPath}/png/searchMark.png"
						class="search" width="60" alt="検索アイコン">
				</button>
			</div>
		</form>
		<div class="container">
			<ul>
				<%
				List<model.Music> list = (List<model.Music>) session.getAttribute("musicList");

				// list にデータがあれば1件ずつループ
				if (list != null && !list.isEmpty()) {
					for (model.Music m : list) {
				%>


				<!-- 曲タイトルをリンクとして表示 -->
				<!-- クリックすると URLベースで再生画面へ -->
				<a
					href="${pageContext.request.contextPath}/PlayMusic?url=<%=java.net.URLEncoder.encode(m.getUrl(), "UTF-8")%>"
					class="music-area btn-flat"> 
					<div class="title"><%=m.getTitle()%></div>
					<div class="artist"><%=m.getArtist()%></div>
					<div class="time"><%= m.getMusicTime() / 100 %>:<%= String.format("%02d", m.getMusicTime() % 100) %></div>

				</a>
				<br>
				<br>
				<%
				} // for の終わり
				} else {
				%>
				<p>曲がありません。</p>
				<%
				}
				%>
			</ul>
		</div>
	</div>
</body>
</html>