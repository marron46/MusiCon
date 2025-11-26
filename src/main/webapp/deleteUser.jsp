<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>アカウント削除</title>
<%-- cssの連携 --%>
<link rel="stylesheet"
	href="webapp/css/style.css">
</head>
<body>
	<div class="container">
		<%-- ページの見出し部分 --%>
	<h1 class="dUser-title">アカウント削除</h1>

		<form action="deleteResult.jsp" method="post">
			<%-- ユーザー名の入力欄 --%>
			<label>ユーザー名</label> <input type="text" name="name" required>

			<%-- パスワードの入力欄 --%>
			<label>パスワード</label><input type="password" name="pass"
				required>

			<%-- 削除ボタン --%>
			<button type="submit">削除</button>
		</form>
	</div>
</body>
</html>