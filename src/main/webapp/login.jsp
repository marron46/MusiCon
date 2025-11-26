<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン</title>
<%-- cssの連携 --%>

</head>
<body>
<h1>ログイン</h1>
<%-- ページの見出し部分 --%>

<form action="Login" method="post">
  <%-- ユーザー名の入力欄 --%>
  ユーザー名<br>
  <input type="text" name="name"><br>

  <%-- パスワードの入力欄 --%>
  パスワード<br>
  <input type="password" name="pass"><br>

  <%-- ログインボタン --%>
  <input type="submit" value="ログイン"><br>

  <%-- 新規登録ページへのリンク --%>
  <a href="UserInsert.jsp">新規登録はこちら</a>
</body>
</html>