<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>曲の再生</title>
<%-- cssの連携 --%>
<link rel="stylesheet" href="css/playMusic.css">
</head>
<body>
	<!-- topページに戻るリンク -->
	<a href="PlayMusic">← topに戻る</a>
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

	<form action="LikeMusic" method="post">
	<imput type="hidden" name="id" value="${music.id}"><!-- name="id" → サーブレットで request.getParameter("id") として受け取れる value="${music.id}" → 現在再生中の曲の ID が代入される  -->
	<button type="submit">いいね！ (${music.likes})</button><!-- ${music.likes}→現在いいね数が表示される -->
	</form>

	

	<br>
	<br>



</body>
</html>