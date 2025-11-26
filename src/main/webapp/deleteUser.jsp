<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>アカウント削除</title>
<%-- cssの連携 --%>

</head>
<body>
<h1>アカウント削除</h1>
<%-- ページの見出し部分 --%>

<form action="Login" method="post">
  <%-- ユーザー名の入力欄 --%>
  ユーザー名<br>
  <input type="text" name="name"><br>

  <%-- パスワードの入力欄 --%>
  パスワード<br>
  <input type="password" name="pass"><br>

  <%-- ログインボタン --%>
  <input type="submit" value="削除"><br>
</form>
</body>
</html>