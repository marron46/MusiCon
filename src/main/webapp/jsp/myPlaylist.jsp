<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.PlaylistItem"%>
<%@ page import="model.Music"%>
<% String userName = (String) session.getAttribute("user_name");
if(userName == null){
	response.sendRedirect(request.getContextPath() + "/index.jsp");
	return;
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>プレイリスト</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/myPlaylist.css">
</head>
<body>
	<canvas id="canvas"></canvas>
	<div class="reverse">
		<a href="${pageContext.request.contextPath}/PlayMusic"> <img
			src="${pageContext.request.contextPath}/png/MusiConLogo.png"
			alt="TOPに戻る" class="reverse-img">
		</a>
	</div>
	<%-- チェックボックス(非表示) --%>
	<input type="checkbox" id="menu-check" class="menu-check">
	<%-- ハンバーガーアイコン --%>
	<label for="menu-check" class="hamburger">
		<div class="line"></div>
		<div class="line"></div>
		<div class="line"></div>
	</label>
	<!-- 半透明オーバーレイ -->
	<div class="overlay"></div>
	<%-- メニュー --%>
	<nav class="side-menu">
	<ul class="user-profile">
		<img src="${pageContext.request.contextPath}/png/musi_usericon.png"
						class="useri" width="36" alt="ユーザーアイコン">
		<li><%=userName%>さん</li>
		</ul>
		<ul class="menu-list">
			<li><a href="${pageContext.request.contextPath}/PlayMusic"
				class="menu">TOP / TOPに戻る</a></li>
			<li><a href="${pageContext.request.contextPath}/MyPlaylist"
				class="menu">Playlist / プレイリスト</a></li>
			<li><a href="${pageContext.request.contextPath}/ShowRanking"
				class="menu">Ranking / ランキング</a></li>
			<li><a
				href="${pageContext.request.contextPath}/jsp/importMusic.jsp"
				class="menu">Add music / 曲アップロード</a></li>
			<li><a href="${pageContext.request.contextPath}/MusicList"
				class="menu">Music list / 楽曲一覧</a></li>
		</ul>
		<ul class="menu-bottom">
			<li><a href="${pageContext.request.contextPath}/Logout"
				class="menu logout">Log out / ログアウト</a></li>
			<li><a
				href="${pageContext.request.contextPath}/jsp/deleteUser.jsp"
				class="menu delete">Delete account / アカウント削除</a></li>
		</ul>
	</nav>

	<!-- ▼ オーバーレイクリックで閉じるスクリプト -->
	<script>
    document.querySelector(".overlay").addEventListener("click", () => {
        document.getElementById("menu-check").checked = false;
    });
</script>
	<h2 class="mBookmark-title">▶ プレイリスト</h2>
<ul class="container">
	<p style="color: green;">
		<%=request.getAttribute("message") != null ? request.getAttribute("message") : ""%>
	</p>

	<%
	List<PlaylistItem> list = (List<PlaylistItem>) session.getAttribute("playlistItems");
	if (list == null || list.isEmpty()) {
	%>
	<p class="NoBookmark">まだプレイリストが空です。</p>
	<%
	} else {
	%>
		<%
		for (PlaylistItem item : list) {
			Music m = item.getMusic();
		%>
		<li><a
			href="${pageContext.request.contextPath}/PlayMusic?playlistMode=true&pos=<%=item.getPos()%>"
			class="music-area btn-flat">
				<div class="title">
					タイトル：<%=m.getTitle()%></div>
				<div class="artist">
					アーティスト：<%=m.getArtist()%></div>
				<div class="id">
					順番：<%=item.getPos() + 1%></div>
		</a>
			<form action="${pageContext.request.contextPath}/MyPlaylist"
				method="post" style="margin-top: 10px;">
				<input type="hidden" name="id" value="<%=m.getId()%>">
				<button type="submit" class="btn-flat"
					style="background: #f7d358; border: none; cursor: pointer;">
					★ プレイリストから外す</button>
			</form> <br> <br></li>
		<hr>
		<%
		}
		%>
	</ul>
	<%
	}
	%>
	<script>
		const rand = function(min, max) {
			  return Math.random() * ( max - min ) + min;
			}

			let canvas = document.getElementById('canvas');
			let ctx = canvas.getContext('2d');

			canvas.width = window.innerWidth;
			canvas.height = window.innerHeight;

			window.addEventListener('resize', () => {
				  canvas.width = window.innerWidth;
				  canvas.height = window.innerHeight;
				});

			let backgroundColors = [ '#000', '#000' ];
			let colors = [
				  ['#3b1d5a', '#6a3fbf'],   // 濃紫 → 明るい紫
				  ['#4b2c82', '#9d6bff'],   // 紫 → ラベンダー
				  ['#2a1b3d', '#7f5af0']    // ダークパープル → ネオン紫
				];

			let count = 25;
			let blur = [ 20, 90 ];
			let radius = [ 100, 300 ];

			ctx.clearRect( 0, 0, canvas.width, canvas.height );
			ctx.globalCompositeOperation = 'lighter';

			let grd = ctx.createLinearGradient(0, canvas.height, canvas.width, 0);
			grd.addColorStop(0, backgroundColors[0]);
			grd.addColorStop(1, backgroundColors[1]);
			ctx.fillStyle = grd;
			ctx.fillRect(0, 0, canvas.width, canvas.height);

			let items = [];

			while(count--) {
			    let thisRadius = rand( radius[0], radius[1] );
			    let thisBlur = rand( blur[0], blur[1] );
			    let x = rand( -300, canvas.width + 300 );
			    let y = rand( -300, canvas.height + 300 );
			    let colorIndex = Math.floor(rand(0, 299) / 100);
			    let colorOne = colors[colorIndex][0];
			    let colorTwo = colors[colorIndex][1];
			    
			    ctx.beginPath();
			    ctx.filter = `blur(${thisBlur}px)`;
			    let grd = ctx.createLinearGradient(x - thisRadius / 2, y - thisRadius / 2, x + thisRadius, y + thisRadius);
			  
			    grd.addColorStop(0, colorOne);
			    grd.addColorStop(1, colorTwo);
			    ctx.fillStyle = grd;
			    ctx.fill();
			    ctx.arc( x, y, thisRadius, 0, Math.PI * 2 );
			    ctx.closePath();
			    
			    let directionX = Math.round(rand(-99, 99) / 100);
			    let directionY = Math.round(rand(-99, 99) / 100);
			  
			    items.push({
			      x: x,
			      y: y,
			      blur: thisBlur,
			      radius: thisRadius,
			      initialXDirection: directionX,
			      initialYDirection: directionY,
			      initialBlurDirection: directionX,
			      colorOne: colorOne,
			      colorTwo: colorTwo,
			      gradient: [ x - thisRadius / 2, y - thisRadius / 2, x + thisRadius, y + thisRadius ],
			    });
			}


			function changeCanvas(timestamp) {
			  ctx.clearRect(0, 0, canvas.width, canvas.height);
			  let adjX = 0.3;
			  let adjY = 0.3;
			  let adjBlur = 0.3;
			  items.forEach(function(item) {
			    
			      if(item.x + (item.initialXDirection * adjX) >= canvas.width && item.initialXDirection !== 0 || item.x + (item.initialXDirection * adjX) <= 0 && item.initialXDirection !== 0) {
			        item.initialXDirection = item.initialXDirection * -1;
			      }
			      if(item.y + (item.initialYDirection * adjY) >= canvas.height && item.initialYDirection !== 0 || item.y + (item.initialYDirection * adjY) <= 0 && item.initialYDirection !== 0) {
			        item.initialYDirection = item.initialYDirection * -1;
			      }
			      
			      if(item.blur + (item.initialBlurDirection * adjBlur) >= radius[1] && item.initialBlurDirection !== 0 || item.blur + (item.initialBlurDirection * adjBlur) <= radius[0] && item.initialBlurDirection !== 0) {
			        item.initialBlurDirection *= -1;
			      }
			    
			      item.x += (item.initialXDirection * adjX);
			      item.y += (item.initialYDirection * adjY);
			      item.blur += (item.initialBlurDirection * adjBlur);
			      ctx.beginPath();
			      ctx.filter = `blur(${item.blur}px)`;
			      let grd = ctx.createLinearGradient(item.gradient[0], item.gradient[1], item.gradient[2], item.gradient[3]);
			      grd.addColorStop(0, item.colorOne);
			      grd.addColorStop(1, item.colorTwo);
			      ctx.fillStyle = grd;
			      ctx.arc( item.x, item.y, item.radius, 0, Math.PI * 2 );
			      ctx.fill();
			      ctx.closePath();
			    
			  });
			  window.requestAnimationFrame(changeCanvas);
			  
			}

			window.requestAnimationFrame(changeCanvas);</script>
</body>
</html>


