<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>新規登録</title>
<%-- cssの連携 --%>

</head>
<body>
<h1>新規登録</h1>
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
</form>

</body>
</html>