<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>MUSICON 採用情報</title>
    <!-- Google Font -->
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Alfa+Slab+One&display=swap" rel="stylesheet">
  
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/common.css">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/recruit.css">
</head>
<body>
<canvas id="canvas"></canvas>
<div class="container">
  <!-- header -->
  <header class="header">
  <a href="${pageContext.request.contextPath}//index.jsp" class="top-btn">TOP</a>
  <img src="${pageContext.request.contextPath}/png/MusiConLogo.png"
		class="icon" width="80" alt="ロゴアイコン">
    <div class="logo">MUSICON Co.</div>
    <div class="sub">採用情報</div>
  </header>

  <!-- hero -->
  <section class="hero">
    <div class="hero-text">
      <h1>
        Singing people.<br>
        Singing the future.
      </h1>
      <p>
        音楽で未来を創る。<br>
        音楽で可能性を拓く。
      </p>
    </div>
    <div class="hero-image">
    <img src="${pageContext.request.contextPath}/png/MusiConRecruit.png"
				alt="採用情報"></div>
  </section>

  <!-- info grid -->
  <section class="info-grid">
    <div class="card">
      <div class="label">募集職種</div>
      <div class="value">
        ミュージシャン<br>
        SE / Webデザイナー<br>
        広報職（広報ツールの作成）
      </div>
    </div>

    <div class="card">
      <div class="label">募集人数</div>
      <div class="value">大多数</div>
    </div>

    <div class="card">
      <div class="label">応募資格</div>
      <div class="value">
        2026年3月31日に修了・卒業見込みの<br>
        高校・中学校の方
      </div>
    </div>

    <div class="card">
      <div class="label">形態</div>
      <div class="value">正職員 / パート・アルバイト</div>
    </div>

    <div class="card">
      <div class="label">勤務時間</div>
      <div class="value">09:15〜10:45（遅刻禁止）</div>
    </div>

    <div class="card">
      <div class="label">給与</div>
      <div class="value">
        高卒：421,980円<br>
        中卒：381,980円
      </div>
    </div>
  </section>
</div>
	<!-- ▼ 背景アニメーション（そのまま） -->
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
</body>
</html>
