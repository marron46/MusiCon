<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>新規登録</title>
<%-- cssの連携 --%>

<%--<link rel="stylesheet"
	href="${pageContext.request.ContextPath}/css/style.css"> --%>
<link rel="stylesheet" href="css/createUser.css">

</head>
<body>
	<div class="reverse">
		<a href="login.jsp" class="top">ログイン画面へ</a>
	</div>
	<div class="container">
		<%-- ページの見出し部分 --%>
		<h1 class="cUser-title">新規登録</h1>

		<form action="CreateUser" method="post">
			<%-- ユーザー名の入力欄 --%>
			<label>ユーザー名</label> <br><input type="text" name="user_name" required><br>

			<%-- パスワードの入力欄 --%>
			<label>パスワード</label><br><input type="password" name="user_pass" required>

			<%-- 登録ボタン --%>
			<button type="submit" class="submit-btn">登録</button>
		</form>
	</div>
</body>
</html>