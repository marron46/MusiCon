<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>曲の追加</title>
<%-- cssの連携 --%>

</head>
<body>
<h1>曲の追加</h1>
<%-- ページの見出し部分 --%>

<form action="Login" method="post">
  <%-- 曲名の入力欄 --%>
  曲名<br>
  <input type="text" name="title"><br>

  <%-- アーティストの入力欄 --%>
  アーティスト<br>
  <input type="text" name="artist"><br>

<%-- リリース日の入力欄 --%>
  リリース日<br>
  <input type="text" name="releaseYear"><br>

  <%-- 追加ボタン --%>
  <input type="submit" value="追加"><br>
</form>
</body>
</html>