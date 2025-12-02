// ====== Audio 再生制御 ======
const audio = document.getElementById("audio");
const playBtn = document.getElementById("play");
const progress = document.getElementById("progress");
const currentLabel = document.getElementById("current");
const durationLabel = document.getElementById("duration");

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
/**
 * 
 */