<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン</title>
<%-- cssの連携 --%>
<link rel="stylesheet" href="webapp/css/style.css">
</head>
<body>
	<div class="container">
		<%-- ページの見出し部分 --%>
		<h1 class="cUser-title">ログイン</h1>

		<form action="LoginUser" method="post">
			<%-- ユーザー名の入力欄 --%>
			<label>ユーザー名</label> <input type="text" name="name" required>

			<%-- パスワードの入力欄 --%>
			<label>パスワード</label><input type="password" name="pass" required>

			<%-- ログインボタン --%>
			<button type="submit">ログイン</button>

			<!-- 新規登録ページへのリンク -->
			<a href="createUser.jsp">新規登録はこちら</a>

		</form>
	</div>
</body>
</html>