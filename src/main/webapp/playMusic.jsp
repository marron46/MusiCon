<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Music"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>曲の再生</title>

<link rel="stylesheet" href="css/playMusic.css">
<script defer src="js/playMusic.js"></script>

</head>

<body>

<a href="PlayMusic" class="back-btn">← topに戻る</a>

<a href="Bookmark?id=<%=m.getId()%>">⭐ブックマーク</a>

<%
    Music music = (Music) request.getAttribute("music");
%>

<!-- ▼ 追加：中央配置用のラッパー ▼ -->
<div class="center-wrapper">
	<div class="player">

    	<!-- 左のジャケット（白無地） -->
    	<div class="album-art"></div>

    	<!-- 右側情報 -->
    	<div class="info">

			<!-- 曲タイトル -->
        	<h1><%=music.getTitle()%></h1>
        
        	<!-- アーティスト名 -->
       	 	<p class="artist"><%=music.getArtist()%></p>

        	<!-- 再生する audio -->
        	<audio id="audio">
            <source src="<%=music.getUrl()%>" type="audio/mpeg">
     		</audio>

        	<!-- 再生ボタン -->
        	<div class="controls">
				<button id="prev">⏮</button>
				<button id="play" class="play">⏸</button>
				<button id="next">⏭</button>
       		</div>

        	<!-- シークバー -->
        	<div class="progress-area">
				<span id="current">0:00</span>
				<input type="range" id="progress" min="0" value="0">
            	<span id="duration">0:00</span>
        	</div>

        	<!-- いいね -->
			<form action="LikeMusic" method="post">
            	<input type="hidden" name="id" value="<%=music.getId()%>">
            	<button type="submit" class="like-btn">
                	いいね！ (<%=music.getLikes()%>)
            	</button>
        	</form>

		</div>

	</div>
	
</div>

</body>
</html>