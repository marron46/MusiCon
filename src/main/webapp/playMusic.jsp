<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>曲の再生</title>
<%-- cssの連携 --%>
<link rel="stylesheet" href="css/style.css">
</head>
<body>

	<!--
    Servlet（PlayMusic）の中で、request.setAttribute("music", music);として送られてきた 1 曲分のデータを受け取っている。 -->
	<%
	model.Music m = (model.Music) request.getAttribute("music");
	%>

	<!-- 曲のタイトル表示 -->
	<h1><%=m.getTitle()%></h1>

	<!-- HTML5 の audio タグで音楽を再生する。controls … 再生/停止/シークバーなどの標準コントロールを表示。source の src は DB に保存されている「ファイルパス」を使用する。 -->
	<audio controls>
		<source src="<%=m.getUrl()%>" type="audio/mpeg">
		ブラウザが audio タグに対応していません。
	</audio>

	<br>
	<br>

	<!-- topページに戻るリンク -->
	<a href="PlayMusic">← topに戻る</a>

</body>
</html>