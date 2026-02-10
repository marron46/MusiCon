<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Music"%>
<%
String userName = (String) session.getAttribute("user_name");
if (userName == null) {
	response.sendRedirect(request.getContextPath() + "/index.jsp");
	return;
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ランキング</title>

<%-- cssの連携 --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/showRanking.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/miniPlayer.css">
<meta name="ctx-path" content="${pageContext.request.contextPath}">
	<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Dela+Gothic+One&display=swap" rel="stylesheet">
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
				class="menu"><div class=now>Ranking / ランキング</div></a></li>
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
	<%-- ページタイトル --%>
	<h1 class="rank-title">人気曲ランキングTOP10</h1>
	</div>
	<div class="container">

		<%-- ランキング一覧 --%>

		<%
		List<Music> list = (List<Music>) session.getAttribute("ranking");

		if (list == null || list.isEmpty()) {
		%>
		<p>検索結果がありません。</p>
		<%
		} else {
		%>
		<ul style="list-style: none;">
			<%
			int count = 0;
			int showing_rank = 0;
			int tmp_like = 0;

			for (model.Music m : list) {
				showing_rank++;
				if (tmp_like == m.getLikes()) {
					count++;
					showing_rank--;
				} else {
					tmp_like = m.getLikes();
					showing_rank = showing_rank + count;
					count = 0;
				}
			%>

			<li>
			
			<%
			if (showing_rank == 1){
				%><div class="rank-first"><%="1位"%>
				/ いいね：<%=m.getLikes()%>回
			</div><%
			}else if(showing_rank == 2){
				%><div class="rank-second"><%="2位"%>
					/ いいね：<%=m.getLikes()%>回
				</div><%
			}else if(showing_rank == 3){
				%><div class="rank-third"><%="3位"%>
					/ いいね：<%=m.getLikes()%>回
				</div><%
			}else{
				%><div class="rankcount"><%=showing_rank + "位"%>
					/ いいね：<%=m.getLikes()%>回
				</div><%
			}
			%> 
				
				<a
				<%// URL が取得できない場合でもページが落ちないようにフォールバックする
String playLink;
if (m.getUrl() != null && !m.getUrl().isEmpty()) {
	playLink = pageContext.getRequest().getServletContext().getContextPath() + "/PlayMusic?url="
			+ java.net.URLEncoder.encode(m.getUrl(), "UTF-8");
} else {
	playLink = pageContext.getRequest().getServletContext().getContextPath() + "/PlayMusic?id=" + m.getId();
}%>
				href="<%=playLink%>"> <a
					href="${pageContext.request.contextPath}/PlayMusic?url=<%=java.net.URLEncoder.encode(m.getUrl(), "UTF-8")%>"
					class="music-area btn-flat">
						<div class="marquee">
							<div class="title"><%=m.getTitle()%></div>
						</div>
						<div class="artist"><%=m.getArtist()%></div>

				</a></li>
			<hr>
			<%
			} // for文の結び
			%>
		</ul>
		<%
		} // else文の結び
		%>

	</div>
	</div>
	<script>
		const rand = (min, max) => Math.random() * (max - min) + min;

		let canvas = document.getElementById('canvas');
		let ctx = canvas.getContext('2d');

		canvas.width = window.innerWidth;
		canvas.height = window.innerHeight;

		window.addEventListener('resize', () => {
			canvas.width = window.innerWidth;
			canvas.height = window.innerHeight;
		});

		let backgroundColors = ['#000','#000'];
		let colors = [
			['#3b1d5a','#6a3fbf'],
			['#4b2c82','#9d6bff'],
			['#2a1b3d','#7f5af0']
		];

		let count = 25;
		let blur = [20,90];
		let radius = [100,300];

		ctx.clearRect(0,0,canvas.width,canvas.height);
		ctx.globalCompositeOperation = 'lighter';

		let grd = ctx.createLinearGradient(0, canvas.height, canvas.width, 0);
		grd.addColorStop(0, backgroundColors[0]);
		grd.addColorStop(1, backgroundColors[1]);
		ctx.fillStyle = grd;
		ctx.fillRect(0,0,canvas.width,canvas.height);

		let items = [];
		while(count--){
			let thisRadius = rand(radius[0], radius[1]);
			let thisBlur = rand(blur[0], blur[1]);
			let x = rand(-300, canvas.width + 300);
			let y = rand(-300, canvas.height + 300);
			let colorIndex = Math.floor(rand(0,299)/100);
			let colorOne = colors[colorIndex][0];
			let colorTwo = colors[colorIndex][1];

			ctx.beginPath();
			ctx.filter = `blur(${thisBlur}px)`;
			let grd = ctx.createLinearGradient(x - thisRadius/2, y - thisRadius/2, x + thisRadius, y + thisRadius);
			grd.addColorStop(0, colorOne);
			grd.addColorStop(1, colorTwo);
			ctx.fillStyle = grd;
			ctx.fill();
			ctx.arc(x,y,thisRadius,0,Math.PI*2);
			ctx.closePath();

			let directionX = Math.round(rand(-99,99)/100);
			let directionY = Math.round(rand(-99,99)/100);

			items.push({
				x:x,
				y:y,
				blur:thisBlur,
				radius:thisRadius,
				initialXDirection:directionX,
				initialYDirection:directionY,
				initialBlurDirection:directionX,
				colorOne:colorOne,
				colorTwo:colorTwo,
				gradient:[x-thisRadius/2, y-thisRadius/2, x+thisRadius, y+thisRadius]
			});
		}

		function changeCanvas(timestamp){
			ctx.clearRect(0,0,canvas.width,canvas.height);
			let adjX = 0.3;
			let adjY = 0.3;
			let adjBlur = 0.3;

			items.forEach(item=>{
				if(item.x + (item.initialXDirection*adjX) >= canvas.width && item.initialXDirection !== 0 || item.x + (item.initialXDirection*adjX) <= 0 && item.initialXDirection !== 0){
					item.initialXDirection *= -1;
				}
				if(item.y + (item.initialYDirection*adjY) >= canvas.height && item.initialYDirection !== 0 || item.y + (item.initialYDirection*adjY) <= 0 && item.initialYDirection !== 0){
					item.initialYDirection *= -1;
				}
				if(item.blur + (item.initialBlurDirection*adjBlur) >= radius[1] && item.initialBlurDirection !== 0 || item.blur + (item.initialBlurDirection*adjBlur) <= radius[0] && item.initialBlurDirection !== 0){
					item.initialBlurDirection *= -1;
				}

				item.x += (item.initialXDirection*adjX);
				item.y += (item.initialYDirection*adjY);
				item.blur += (item.initialBlurDirection*adjBlur);

				ctx.beginPath();
				ctx.filter = `blur(${item.blur}px)`;
				let grd = ctx.createLinearGradient(item.gradient[0], item.gradient[1], item.gradient[2], item.gradient[3]);
				grd.addColorStop(0, item.colorOne);
				grd.addColorStop(1, item.colorTwo);
				ctx.fillStyle = grd;
				ctx.arc(item.x,item.y,item.radius,0,Math.PI*2);
				ctx.fill();
				ctx.closePath();
			});
			window.requestAnimationFrame(changeCanvas);
		}
		window.requestAnimationFrame(changeCanvas);
	</script>
	<!-- ▼ 曲タイトルスクロール -->
	<script>
	window.addEventListener('DOMContentLoaded', () => {
	    const marquees = document.querySelectorAll('.marquee');

	    // 枠内判定
	    marquees.forEach(marquee => {
	        const title = marquee.querySelector('.title');

	        requestAnimationFrame(() => {
	            const textWidth = title.scrollWidth;
	            const boxWidth  = marquee.clientWidth;

	            if(textWidth > boxWidth){
	                title.dataset.marquee = "true"; // スクロール対象
	                title.style.textAlign = 'left';
	            } else {
	                title.dataset.marquee = "false"; // 枠内に収まる
	                title.style.textAlign = 'center';
	            }
	        });
	    });

	    // ON/OFF切替（5秒ごと）
	    let active = false;
	    setInterval(() => {
	        active = !active;
	        marquees.forEach(marquee => {
	            const title = marquee.querySelector('.title');
	            if(title.dataset.marquee === "true"){
	                if(active){
	                    title.classList.add('is-marquee');
	                } else {
	                    title.classList.remove('is-marquee');
	                    title.style.transform = 'translateX(0)';
	                }
	            }
	        });
	    }, 5000);
	});
	</script>
<script src="${pageContext.request.contextPath}/js/miniPlayer.js?v=2"></script>
</body>
</html>