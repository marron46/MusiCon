<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String userName = (String) session.getAttribute("user_name");
if (userName == null) {
	response.sendRedirect(request.getContextPath() + "/index.jsp");
	return;
}
String metadataError = (String) request.getAttribute("metadataError");
String savedFile = (String) request.getAttribute("saved_file");
String genre = (String) request.getAttribute("genre");
String prefillTitle = (String) request.getAttribute("prefillTitle");
String prefillArtist = (String) request.getAttribute("prefillArtist");
Integer prefillReleaseYear = (Integer) request.getAttribute("prefillReleaseYear");
Integer prefillMusicTime = (Integer) request.getAttribute("prefillMusicTime");
if (prefillTitle == null) prefillTitle = "";
if (prefillArtist == null) prefillArtist = "";
if (prefillReleaseYear == null) prefillReleaseYear = 0;
if (prefillMusicTime == null) prefillMusicTime = 0;
// 直接アクセスや属性欠損時はアップロード画面へ
if (savedFile == null || savedFile.isEmpty() || genre == null) {
	response.sendRedirect(request.getContextPath() + "/jsp/importMusic.jsp");
	return;
}
// HTML属性用の簡易エスケープ
String savedFileEsc = savedFile == null ? "" : savedFile.replace("&", "&amp;").replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;");
String genreEsc = genre == null ? "" : genre.replace("&", "&amp;").replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;");
String prefillTitleEsc = prefillTitle == null ? "" : prefillTitle.replace("&", "&amp;").replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;");
String prefillArtistEsc = prefillArtist == null ? "" : prefillArtist.replace("&", "&amp;").replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;");
String metadataErrorEsc = metadataError == null ? "" : metadataError.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>手動で曲情報を入力</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/importMusic.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/miniPlayer.css">
<meta name="ctx-path" content="${pageContext.request.contextPath}">
</head>
<body>
<canvas id="canvas"></canvas>
	<div class="reverse">
		<a href="${pageContext.request.contextPath}/PlayMusic"> <img
			src="${pageContext.request.contextPath}/png/MusiConLogo.png"
			alt="TOPに戻る" class="reverse-img">
		</a>
	</div>
	<input type="checkbox" id="menu-check" class="menu-check">
	<label for="menu-check" class="hamburger">
		<div class="line"></div>
		<div class="line"></div>
		<div class="line"></div>
	</label>
	<div class="overlay"></div>
	<nav class="side-menu">
		<ul class="user-profile">
		<img src="${pageContext.request.contextPath}/png/musi_usericon.png"
						class="useri" width="36" alt="ユーザーアイコン">
		<li><%=userName%>さん</li>
		</ul>
		<ul class="menu-list">
			<li><a href="${pageContext.request.contextPath}/PlayMusic" class="menu">TOP / TOPに戻る</a></li>
			<li><a href="${pageContext.request.contextPath}/MyPlaylist" class="menu">Playlist / プレイリスト</a></li>
			<li><a href="${pageContext.request.contextPath}/ShowRanking" class="menu">Ranking / ランキング</a></li>
			<li><a href="${pageContext.request.contextPath}/jsp/importMusic.jsp" class="menu"><div class=now>Add music / 曲アップロード</div></a></li>
			<li><a href="${pageContext.request.contextPath}/MusicList" class="menu">Music list / 楽曲一覧</a></li>
		</ul>
		<ul class="menu-bottom">
			<li><a href="${pageContext.request.contextPath}/Logout" class="menu logout">Log out / ログアウト</a></li>
			<li><a href="${pageContext.request.contextPath}/jsp/deleteUser.jsp" class="menu delete">Delete account / アカウント削除</a></li>
		</ul>
	</nav>
	<script>
		document.querySelector(".overlay").addEventListener("click", () => {
			document.getElementById("menu-check").checked = false;
		});
	</script>
	<div class="container">
		<h1 class="iMusic-title">曲情報を手動で入力</h1>
		<% if (metadataError != null && !metadataError.isEmpty()) { %>
		<p class="error-msg"><%= metadataErrorEsc %></p>
		<% } %>
		<p class="form-note">以下の項目を入力して登録を完了してください。</p>
		<form action="${pageContext.request.contextPath}/ImportMusic" method="post" class="grid">
			<input type="hidden" name="manual" value="1">
			<input type="hidden" name="saved_file" value="<%= savedFileEsc %>">
			<input type="hidden" name="genre" value="<%= genreEsc %>">
			<div class="form-item">
				<input type="text" name="title" placeholder="タイトル" required
					value="<%= prefillTitleEsc %>">
			</div>
			<div class="form-item">
				<input type="text" name="artist" placeholder="アーティスト" required
					value="<%= prefillArtistEsc %>">
			</div>
			<div class="form-item">
				<input type="text" name="releaseYear" placeholder="発売年"
					value="<%= prefillReleaseYear != 0 ? String.valueOf(prefillReleaseYear) : "" %>">
			</div>
			<div class="form-item">
				<input type="number" name="music_time" placeholder="再生時間（秒）" min="0" required
					value="<%= prefillMusicTime != 0 ? String.valueOf(prefillMusicTime) : "" %>">
			</div>
			<div class="form-item" style="grid-column: span 2; text-align: center;">
				<input type="submit" value="登録する" class="submit-btn">
			</div>
		</form>
	</div>
	<script>
		const rand = function(min, max) { return Math.random() * ( max - min ) + min; };
		let canvas = document.getElementById('canvas');
		let ctx = canvas.getContext('2d');
		canvas.width = window.innerWidth;
		canvas.height = window.innerHeight;
		window.addEventListener('resize', () => { canvas.width = window.innerWidth; canvas.height = window.innerHeight; });
		let backgroundColors = [ '#000', '#000' ];
		let colors = [ ['#3b1d5a', '#6a3fbf'], ['#4b2c82', '#9d6bff'], ['#2a1b3d', '#7f5af0'] ];
		let count = 25, blur = [ 20, 90 ], radius = [ 100, 300 ];
		ctx.clearRect( 0, 0, canvas.width, canvas.height );
		ctx.globalCompositeOperation = 'lighter';
		let grd = ctx.createLinearGradient(0, canvas.height, canvas.width, 0);
		grd.addColorStop(0, backgroundColors[0]); grd.addColorStop(1, backgroundColors[1]);
		ctx.fillStyle = grd; ctx.fillRect(0, 0, canvas.width, canvas.height);
		let items = [];
		while(count--) {
			let thisRadius = rand( radius[0], radius[1] ), thisBlur = rand( blur[0], blur[1] );
			let x = rand( -300, canvas.width + 300 ), y = rand( -300, canvas.height + 300 );
			let colorIndex = Math.floor(rand(0, 299) / 100);
			let colorOne = colors[colorIndex][0], colorTwo = colors[colorIndex][1];
			ctx.beginPath(); ctx.filter = 'blur('+thisBlur+'px)';
			let g = ctx.createLinearGradient(x - thisRadius/2, y - thisRadius/2, x + thisRadius, y + thisRadius);
			g.addColorStop(0, colorOne); g.addColorStop(1, colorTwo);
			ctx.fillStyle = g; ctx.fill(); ctx.arc(x, y, thisRadius, 0, Math.PI*2); ctx.closePath();
			let dx = Math.round(rand(-99,99)/100), dy = Math.round(rand(-99,99)/100);
			items.push({ x, y, blur: thisBlur, radius: thisRadius, dx, dy, db: dx, colorOne, colorTwo,
				gradient: [x-thisRadius/2, y-thisRadius/2, x+thisRadius, y+thisRadius] });
		}
		function animate() {
			ctx.clearRect(0, 0, canvas.width, canvas.height);
			const adj = 0.3;
			items.forEach(function(item) {
				if (item.x + item.dx >= canvas.width || item.x + item.dx <= 0) item.dx *= -1;
				if (item.y + item.dy >= canvas.height || item.y + item.dy <= 0) item.dy *= -1;
				if (item.blur + item.db >= radius[1] || item.blur + item.db <= radius[0]) item.db *= -1;
				item.x += item.dx; item.y += item.dy; item.blur += item.db;
				ctx.beginPath(); ctx.filter = 'blur('+item.blur+'px)';
				let g = ctx.createLinearGradient(item.gradient[0], item.gradient[1], item.gradient[2], item.gradient[3]);
				g.addColorStop(0, item.colorOne); g.addColorStop(1, item.colorTwo);
				ctx.fillStyle = g; ctx.arc(item.x, item.y, item.radius, 0, Math.PI*2); ctx.fill(); ctx.closePath();
			});
			window.requestAnimationFrame(animate);
		}
		window.requestAnimationFrame(animate);
	</script>
<script src="${pageContext.request.contextPath}/js/miniPlayer.js?v=2"></script>
</body>
</html>
