<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
	<div class="wrapper">
		<div class="logo">
			<img src="${pageContext.request.contextPath}/png/MusiConLogo.png"
				alt="サイトのロゴアイコン">
		</div>

		<div class="container">
			<h1 class="cUser-title">ログイン</h1>

			<!-- エラーメッセージの表示 -->
			<c:if test="${not empty requestScope.errorMessage}">
				<p class="error-message">${requestScope.errorMessage}</p>
			</c:if>

			<form action="${pageContext.request.contextPath}/LoginUser"
				method="post">
				<input type="text" name="user_name" placeholder="ユーザー名" required
					class="${requestScope.userNameError != null && requestScope.userNameError ? 'error' : ''}"><br>

				<input type="password" name="user_pass" placeholder="パスワード" required
					class="${requestScope.userPassError != null && requestScope.userPassError ? 'error' : ''}"><br>


				<button type="submit" class="login-btn">ログイン</button>
				<br> <a
					href="${pageContext.request.contextPath}/jsp/createUser.jsp">新規登録はこちら</a>
			</form>
		</div>
	</div>
</body>
</html>
