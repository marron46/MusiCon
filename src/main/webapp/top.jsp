<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>トップ</title>
<%-- cssの連携 --%>

</head>
<body>

<form action="Login" method="post">
  <%-- ハンバーガーリスト --%>
  <div id="hamburger" class="hamburger">
  	☰
  </div>
  
  <%-- サイドバー --%>
  <div id="sidebar" class="sidebar"></div>
  	<a href="myBookmark.jsp" class="menu">マイページ</a>
  	<a href="showRanking.jsp" class="menu">ランキング</a>
  	<a href="importMusic.jsp" class="menu">曲追加</a>
  	<a href="無記入" class="menu">ログアウト</a>
  	<a href="deleteUser.jsp" class="menu">アカウント削除</a>
  </div>
  
  <div class="main">
  
  <%-- 検索バー --%>
  <div class "search-area">
  	<img src="無記入" class="無記入">
  	<input type="text" class="searchbox">
  </div>

	<%-- 曲リスト --%>
	
	無記入
	
	
	
  <%-- ログインボタン --%>
  <input type="submit" value="ログイン"><br>

<script>
<%--ハンバーガークリックでサイドバー開閉 --%>
document.getElementById("hamburger").oneclick = function () {
	document.getElementById("sidebar").classList.toggle("open");
};
</script>
</body>
</html>