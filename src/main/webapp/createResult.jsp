<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.User" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザー登録結果</title>
<%-- cssの連携 --%>
<link rel="stylesheet" href="css/createResult.css">
</head>
<body>
	<div class="container">
		<h1>ユーザー登録結果</h1>
		<!-- ページのメインタイトル -->
		<p>登録に成功しました</p>

		<p><%=(String)session.getAttribute("user_name")%>さんはじめまして
		</p>
		<a href="login.jsp" class="log-btn">ログイン画面へ</a>

	</div>
</body>
</html>