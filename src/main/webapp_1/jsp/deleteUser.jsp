<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>アカウント削除</title>
<%-- cssの連携 --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/deleteUser.css">
</head>
<body>
	<div class="reverse">
		<a href="${pageContext.request.contextPath}/PlayMusic"> 
		<img src="${pageContext.request.contextPath}/png/MusiConLogo.png"
			alt="TOPに戻る" class="reverse-img">
		</a>
	</div>
	<div class="container">
		<%-- ページの見出し部分 --%>
		<h1 class="dUser-title">アカウント削除</h1>
		<h4 class="description">削除するユーザー名とパスワードを入力してください。</h4>
		<form action="${pageContext.request.contextPath}/DeleteUser"
			method="post">
			<div class="form-area">
				<%-- ユーザー名の入力欄 --%>
				<%-- <label>ユーザー名</label><br>--%>
				<input type="text" name="user_name" placeholder="ユーザー名" required><br>

				<%-- パスワードの入力欄 --%>
				<%-- <label>パスワード</label><br>--%>
				<input type="password" name="user_pass" placeholder="パスワード" required><br>

				<%-- 削除ボタン --%>
				<button type="submit" class="submit-btn">削除</button>
			</div>
		</form>
	</div>
</body>
</html>