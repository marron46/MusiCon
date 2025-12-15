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
