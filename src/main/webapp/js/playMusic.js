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
// 初回読み込み時もチェック
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




/* =====================================================
   イコライザー
===================================================== */
window.addEventListener("DOMContentLoaded", () => {

  const audio = document.getElementById("audio");
  const canvas = document.getElementById("equalizer");
  const ctx = canvas.getContext("2d");
  
  // CanvasサイズをCSSと一致させる
  canvas.width = canvas.offsetWidth;
  canvas.height = canvas.offsetHeight;


  /* ▼ Canvasの実サイズをCSSと一致させる */
  canvas.width = canvas.offsetWidth;
  canvas.height = canvas.offsetHeight;

  /* ▼ Web Audio API 初期化 */
  const AudioContext = window.AudioContext || window.webkitAudioContext;
  const audioCtx = new AudioContext();

  let source; // MediaElementSourceは1回しか作れない

  const analyser = audioCtx.createAnalyser();
  analyser.fftSize = 64;

  analyser.connect(audioCtx.destination);

  const bufferLength = analyser.frequencyBinCount;
  const dataArray = new Uint8Array(bufferLength);

  /* ==========================================
     描画処理
  ========================================== */
  function draw() {
    analyser.getByteFrequencyData(dataArray);

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    const barWidth = canvas.width / bufferLength;

    for (let i = 0; i < bufferLength; i++) {
      const value = dataArray[i];
      const barHeight = (value / 255) * canvas.height;

      const x = i * barWidth;
      const y = canvas.height - barHeight;

	  ctx.fillStyle = "white";
      ctx.fillRect(x, y, barWidth - 2, barHeight);
    }

    requestAnimationFrame(draw);
  }

  /* ==========================================
     再生時にAudioContextを有効化
  ========================================== */
  audio.addEventListener("play", () => {

    if (audioCtx.state === "suspended") {
      audioCtx.resume();
    }

    /* ▼ sourceは1回だけ生成 */
    if (!source) {
      source = audioCtx.createMediaElementSource(audio);
      source.connect(analyser);
    }

    draw();
  });

});

/* =========================================
   イコライザー
========================================= */

(() => {

  const audio = document.getElementById("audio");
  const playBtn = document.getElementById("play");
  const canvas = document.getElementById("equalizer");
  const ctx = canvas.getContext("2d");

  // Canvasサイズを実サイズに合わせる（重要）
  canvas.width = canvas.offsetWidth;
  canvas.height = canvas.offsetHeight;

  const AudioContext = window.AudioContext || window.webkitAudioContext;
  const audioCtx = new AudioContext();

  const analyser = audioCtx.createAnalyser();
  analyser.fftSize = 64;

  let sourceCreated = false;

  const bufferLength = analyser.frequencyBinCount;
  const dataArray = new Uint8Array(bufferLength);

  function draw() {
    analyser.getByteFrequencyData(dataArray);
    ctx.clearRect(0, 0, canvas.width, canvas.height);

	const gap = 10; /* バーの隙間 */
	const barWidth = (canvas.width - gap * (bufferLength - 1)) / bufferLength;


	let x = 0;

	for (let i = 0; i < bufferLength; i++) {
	  const h = (dataArray[i] / 255) * canvas.height;

	  ctx.fillRect(
	    x,
	    canvas.height - h,
	    barWidth,
	    h
	  );

	  x += barWidth + gap; /* ← 隙間を考慮 */
	}

    requestAnimationFrame(draw);
  }

  /* =====================================
     再生ボタンから直接起動する
  ===================================== */
  playBtn.addEventListener("click", () => {

    // AudioContext をユーザー操作で解除
    if (audioCtx.state === "suspended") {
      audioCtx.resume();
    }

    // MediaElementSource は1回だけ
    if (!sourceCreated) {
      const source = audioCtx.createMediaElementSource(audio);
      source.connect(analyser);
      analyser.connect(audioCtx.destination);
      sourceCreated = true;
    }

    draw(); // ← 強制的に描画開始
  });

})();



