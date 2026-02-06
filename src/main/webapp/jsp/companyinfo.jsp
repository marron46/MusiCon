<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>MUSICON 企業情報</title>
    <!-- Google Font -->
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Alfa+Slab+One&display=swap" rel="stylesheet">
  
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/companyinfo.css">
</head>
<body>
<canvas id="canvas"></canvas>
<div class="container">
  <!-- header -->
  <header class="header">
  <a href="${pageContext.request.contextPath}//index.jsp" class="top-btn">TOP</a>
  <img src="${pageContext.request.contextPath}/png/MusiConLogo.png"
		class="icon" width="80" alt="ロゴアイコン">
    <div class="logo">MUSICON Co. 企業情報</div>
  </header>

  <!-- hero -->
  <section class="hero">
    <div class="hero-text">
      <h1>
        MUSICONの使命は世界中の<br>
        音楽を窃取し、世界中の人々が<br>
        アクセスできて使えるように<br>
        することです(笑)
      </h1>
    </div>
    <div class="hero-image">
    <img src="${pageContext.request.contextPath}/png/MusiConRecruit.png"
				alt="採用情報"></div>
  </section>

  <!-- info grid -->
  <section class="info-grid">
    <div class="card">
      <div class="label">人材募集</div>
      <div class="value">
      	Musiconは私たちと一緒に働いてくれる方を募集しています。<br><br>
      	<a href="${pageContext.request.contextPath}/jsp/recruit.jsp" class="work-btn">共に働く</a></p>
      </div>
    </div>

    <div class="card">
      <div class="label">所在地</div>
      <div class="value">
      	東京本社：東京都千代田区千代田1-1<br>
      	福岡支部：福岡県北九州市小倉南区若園５丁目１−１<br>
      	南極支部：XHWJ+6VW 昭和基地 Showa Station</div>
      	
    </div>

    <div class="card">
      <div class="label">お問い合わせ</div>
      <div class="value">
        電話番号：0261-0273-0236<br>
        インスタ：@MusiconOfficial
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
