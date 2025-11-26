<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>曲の追加</title>
<%-- cssの連携 --%>
<link rel="stylesheet"
	href="webapp/css/style.css">
</head>
<body>
	<div class="container">
		<%-- ページの見出し部分 --%>
		<h1 class="cUser-title">曲の追加</h1>

		<form action="top.jsp" method="post">
			<%-- 曲名の入力欄 --%>
			<label>曲名</label> <input type="text" name="musicName" required>

			<%-- アーティストの入力欄 --%>
			<label>アーティスト</label><input type="artist" name="pass"
				required>
				
			<%-- リリース日の入力欄 --%>
			<label>リリース日</label><input type="releaseYear" name="pass"
				required>
				
			<%-- 追加ボタン --%>
			<button type="submit">追加</button>
		</form>
	</div>
</body>
</html>