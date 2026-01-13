// ====== Audio 再生制御 ======
const audio = document.getElementById("audio");
const playBtn = document.getElementById("play");
const progress = document.getElementById("progress");
const currentLabel = document.getElementById("current");
const durationLabel = document.getElementById("duration");

// ▼ 横幅変更対応：ウィンドウ幅に応じて body にクラス付与
function handleResize() {
    if (window.innerWidth < 1200) {
        document.body.classList.add("narrow");
    } else {
        document.body.classList.remove("narrow");
    }
}

window.addEventListener("resize", handleResize);
window.addEventListener("DOMContentLoaded", handleResize);
/*function handleResize() {
    if(window.innerWidth < 1200){
        document.body.classList.add("narrow");
    } else {
        document.body.classList.remove("narrow");
    }
}

window.addEventListener("resize", handleResize);
// 初回読み込み時もチェックA
window.addEventListener("DOMContentLoaded", handleResize);*/

// 再生/停止
playBtn.addEventListener("click", () => {
	if (audio.paused) {
		audio.play();
		playBtn.textContent = "⏸";
	} else {
		audio.pause();
		playBtn.textContent = "▶";
	}
});

// 曲の長さ読み込み
audio.addEventListener("loadedmetadata", () => {
	progress.max = audio.duration;
	durationLabel.textContent = formatTime(audio.duration);
});

// 再生中に更新
audio.addEventListener("timeupdate", () => {
	progress.value = audio.currentTime;
	currentLabel.textContent = formatTime(audio.currentTime);
});

// シーク
progress.addEventListener("input", () => {
	audio.currentTime = progress.value;
});

function formatTime(t) {
	const m = Math.floor(t / 60);
	const s = Math.floor(t % 60).toString().padStart(2, '0');
	return `${m}:${s}`;
}

// 音量バーで変更する関数
document.addEventListener("DOMContentLoaded", () => {

	const audio = document.getElementById("audio");
	const volumeSlider = document.getElementById("volume");
	const volumeIcon = document.getElementById("volume-icon");

	// ▼ 前の音量を保存する変数
	let lastVolume = volumeSlider.value;

	// ▼ 初期値
	audio.volume = volumeSlider.value;

	// ▼ 音量スライダー操作
	volumeSlider.addEventListener("input", () => {
		const v = Number(volumeSlider.value);
		audio.volume = v;

		// ミュート解除時のために保存
		if (v > 0) lastVolume = v;

		updateIcon(v);
	});

	// ▼ アイコンクリックでミュート / 解除
	volumeIcon.addEventListener("click", () => {
		if (audio.volume > 0) {
			// ミュート
			lastVolume = audio.volume;  // 元の音量を記憶
			audio.volume = 0;
			volumeSlider.value = 0;
			updateIcon(0);
		} else {
			// ミュート解除（元の音量に戻す）
			audio.volume = lastVolume;
			volumeSlider.value = lastVolume;
			updateIcon(lastVolume);
		}
	});

	// ▼ アイコン更新関数
	function updateIcon(v) {
		if (v === 0) {
			volumeIcon.textContent = "🔇";
		} else if (v < 0.23) {
			volumeIcon.textContent = "🔈";
		} else if (v < 0.47) {
			volumeIcon.textContent = "🔉";
		} else {
			volumeIcon.textContent = "🔊";
		}
	}
});



/* =========================================
   イコライザー
========================================= */

window.addEventListener("DOMContentLoaded", () => {

  /* ===============================
     要素取得
  =============================== */
  const audio   = document.getElementById("audio");
  const playBtn = document.getElementById("play");
  const canvas  = document.getElementById("equalizer");
  const ctx     = canvas.getContext("2d");

  /* ===============================
     見た目設定（ここ触る）
  =============================== */
  const BAR_WIDTH = 12; // バー1本の太さ(px)
  const GAP       = 8;  // バー同士の隙間(px)

  /* ===============================
     CanvasサイズをCSSと同期
     （これをしないとボケる）
  =============================== */
  function resizeCanvas() {
    canvas.width  = canvas.offsetWidth;
    canvas.height = canvas.offsetHeight;
  }
  resizeCanvas();
  window.addEventListener("resize", resizeCanvas);

  /* ===============================
     Web Audio API 初期化
  =============================== */
  const AudioContext = window.AudioContext || window.webkitAudioContext;
  const audioCtx = new AudioContext();

  // 音の解析担当
  const analyser = audioCtx.createAnalyser();
  analyser.fftSize = 128; // 値を上げるとバーが細かくなる

  // 周波数データ格納用
  const dataArray = new Uint8Array(analyser.frequencyBinCount);

  // MediaElementSource は1回しか作れないのでフラグ管理
  let sourceCreated = false;

  /* ==============================
  	Web Audio の接続処理 
   =============================== */
  function setupAudio() {
    if (!sourceCreated) {
      const source = audioCtx.createMediaElementSource(audio);
      source.connect(analyser);
      analyser.connect(audioCtx.destination);
      sourceCreated = true;
    }
  }

  /* ===============================
     描画処理
  =============================== */
  let isDrawing = false;

  function draw() {
    if (!isDrawing) return;

    analyser.getByteFrequencyData(dataArray);
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.fillStyle = "rgba(255, 255, 255, 0.3)";

    const unit = BAR_WIDTH + GAP;
    const maxBars = Math.floor(canvas.width / unit);
    const barCount = Math.min(maxBars, dataArray.length);
    const totalWidth = barCount * unit - GAP;
    const offsetX = (canvas.width - totalWidth) / 2;

    for (let i = 0; i < barCount; i++) {
      const raw = dataArray[i];
      const centerBoost = Math.sin((i / (barCount - 1)) * Math.PI);
      const h = (raw / 255) * canvas.height * (0.3 + centerBoost * 0.9) * 0.9;

      ctx.beginPath();
      ctx.roundRect(
        offsetX + i * unit,
        canvas.height - h,
        BAR_WIDTH,
        h,
        6
      );
      ctx.fill();
    }

    requestAnimationFrame(draw);
  }


  /* ===============================
       再生開始時
    =============================== */
	
	audio.addEventListener("play", () => {
	  audioCtx.resume();
	  setupAudio();
	  isDrawing = true;
	  draw();
	});

	audio.addEventListener("pause", () => {
	  isDrawing = false;
	});

});





