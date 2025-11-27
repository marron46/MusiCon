<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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

		<%
		if (user_name != null) {
		%>
		<!-- 
      loginUserがnullでない場合（＝ログイン成功時）
      ユーザー名を表示して、メインページへのリンクを出す
    -->
		<p>登録に成功しました</p>
		
		<p>
			"<%=user_name%>"さんはじめまして
		</p>
		<a href="login.jsp" class="cr-btn">ログイン画面へ</a>

		<%
		} else {
		%>
		<!-- 
      loginUserがnullの場合（＝ログイン失敗時）
      エラーメッセージとトップページへのリンクを表示
    -->
		<p>登録に失敗しました</p>
		<a href="createUser.jsp" class="cr-btn">ユーザー登録画面へ</a>
		<%
		}
		%>
	</div>
</body>
</html>