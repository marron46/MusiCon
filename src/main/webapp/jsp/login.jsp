<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン</title>
<%-- cssの連携 --%>
<link rel="stylesheet" href="css/login.css">
</head>
<body>
	<div class="container">
		<%-- ページの見出し部分 --%>
		<h1 class="cUser-title">ログイン</h1>

		<form action="LoginUser" method="post">
			<%-- ユーザー名の入力欄 --%>
			<%-- <label>ユーザー名</label><br>--%> <input type="text" name="user_name" placeholder="ユーザー名"
				required><br>

			<%-- パスワードの入力欄 --%>
			<%-- <label>パスワード</label><br>--%> <input type="password" name="user_pass" placeholder="パスワード"
				required><br>

			<%-- ログインボタン --%>
			<button type="submit" class="login-btn">ログイン</button>
			<br>

			<!-- 新規登録ページへのリンク -->
			<a href="createUser.jsp">新規登録はこちら</a>

		</form>
	</div>
</body>
</html>