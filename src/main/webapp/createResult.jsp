<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザー登録結果</title>
<%-- cssの連携 --%>
<link rel="stylesheet"
	href="webapp/css/style.css">
</head>
<body>
	<h1>ユーザー登録結果</h1>
	<!-- ページのメインタイトル -->

	<% if (user_name != null){ %>
	<!-- 
      loginUserがnullでない場合（＝ログイン成功時）
      ユーザー名を表示して、メインページへのリンクを出す
    -->
	<p>ログインに成功しました</p>
	<p>
		"<%= user_name %>
		"さんはじめまして
	</p>
	<a href="login.jsp">ログイン画面へ</a>

	<% } else { %>
	<!-- 
      loginUserがnullの場合（＝ログイン失敗時）
      エラーメッセージとトップページへのリンクを表示
    -->
	<p>ログインに失敗しました</p>
	<a href="createUser.jsp">ユーザー登録画面へ</a>
	<% } %>

</body>
</html>