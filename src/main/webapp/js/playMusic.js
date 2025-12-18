// ====== Audio 再生制御 ======
const audio = document.getElementById("audio");
const playBtn = document.getElementById("play");
const loopBtn = document.getElementById("loop");
const progress = document.getElementById("progress");
const currentLabel = document.getElementById("current");
const durationLabel = document.getElementById("duration");

// ====== ループ（1曲リピート）切替 ======
(() => {
	if (!audio || !loopBtn) return;

	function applyLoopState(enabled) {
		audio.loop = !!enabled;
		loopBtn.classList.toggle("toggle-active", !!enabled);
		loopBtn.setAttribute("aria-pressed", enabled ? "true" : "false");
	}

	// 初期状態（localStorageが使えない環境でも落ちないように）
	try {
		const saved = localStorage.getItem("music_loop") === "true";
		applyLoopState(saved);
	} catch (e) {
		applyLoopState(false);
	}

	loopBtn.addEventListener("click", () => {
		const next = !audio.loop;
		applyLoopState(next);
		try {
			localStorage.setItem("music_loop", next ? "true" : "false");
		} catch (e) {
			// 保存できなくても動作は継続
		}
	});
})();

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




/* =========================================
   イコライザー（重複実装を統合：MediaElementSource は 1回だけ）
========================================= */

(() => {

  const audio = document.getElementById("audio");
  const playBtn = document.getElementById("play");
  const canvas = document.getElementById("equalizer");
  if (!audio || !playBtn || !canvas) return;
  const ctx = canvas.getContext("2d");

  // Canvasサイズを実サイズに合わせる（重要）
  canvas.width = canvas.offsetWidth;
  canvas.height = canvas.offsetHeight;

  const AudioContext = window.AudioContext || window.webkitAudioContext;
  const audioCtx = new AudioContext();

  const analyser = audioCtx.createAnalyser();
  analyser.fftSize = 64;

  let sourceCreated = false;
  let drawing = false;

  const bufferLength = analyser.frequencyBinCount;
  const dataArray = new Uint8Array(bufferLength);

  function draw() {
    analyser.getByteFrequencyData(dataArray);
    ctx.clearRect(0, 0, canvas.width, canvas.height);

	const gap = 10; /* バーの隙間 */
	const barWidth = (canvas.width - gap * (bufferLength - 1)) / bufferLength;

	let x = 0;
	ctx.fillStyle = "white";

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
      try {
        const source = audioCtx.createMediaElementSource(audio);
        source.connect(analyser);
        analyser.connect(audioCtx.destination);
        sourceCreated = true;
      } catch (e) {
        // 既に別の MediaElementSourceNode に接続済みの場合（InvalidStateError）
        // 例外で他の処理が止まらないようにする
        sourceCreated = true;
      }
    }

    if (!drawing) {
      drawing = true;
      draw(); // 描画開始（多重起動防止）
    }
  });

})();



