<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.User"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザー登録結果</title>
<!-- cssの連携 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/createResult.css">
</head>
<body>
	<div class="container">
		<div class="message">
			<div class="greeting">
				<h1>登録に成功しました</h1>
				
				<h2><%= (String) session.getAttribute("user_name") %>さん<br>
					はじめまして
				</h2>
			</div>
			<a href="${pageContext.request.contextPath}/jsp/login.jsp" class="log-btn">ログイン画面へ</a>
		</div>
	</div>
</body>
</html>
