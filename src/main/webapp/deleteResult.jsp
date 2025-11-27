<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザー削除</title>
<%-- cssの連携 --%>
<link rel="stylesheet"
	href="webapp/css/style.css">
</head>
<body>
	<a href="top.jsp" class="top">TOP</a>
	<p>
		"user_name"さんを<br> 削除しました
	</p>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.User" %>

==================================更新してください。
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザー登録結果</title>
<%-- cssの連携 --%>
<link rel="stylesheet" href="webapp/css/createResult.css">
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
==================================