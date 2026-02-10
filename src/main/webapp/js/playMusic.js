console.log("[playMusic] ★ スクリプト読み込み完了 ★");

window.addEventListener("DOMContentLoaded", () => {

  console.log("[playMusic] DOMContentLoaded 開始");
  const audio = document.getElementById("audio");
  const playBtn = document.getElementById("play");
  const loopBtn = document.getElementById("loop");
  const progress = document.getElementById("progress");
  const currentLabel = document.getElementById("current");
  const durationLabel = document.getElementById("duration");
  const volumeSlider = document.getElementById("volume");
  const volumeIcon = document.getElementById("volume-icon");
  const canvas = document.getElementById("equalizer");

  /* ── ミニプレイヤー状態管理 ── */
  const playerContainer = document.getElementById("player-container");
  console.log("[playMusic] DOMContentLoaded – playerContainer:", !!playerContainer);

  /*  再生ページに到着 → "active" フラグだけ消す
      （他のキーは復元用に残す） */
  sessionStorage.removeItem("miniPlayer_active");

  /* ── ミニプレイヤーから戻ってきた場合の復元 ── */
  try {
    const resumeFromMini = sessionStorage.getItem("miniPlayer_resumeFromMini") === "true";
    if (resumeFromMini) {
      console.log("[playMusic] ミニプレイヤーから復帰");
      const savedTime = parseFloat(sessionStorage.getItem("miniPlayer_currentTime")) || 0;
      const wasMiniPlaying = sessionStorage.getItem("miniPlayer_playing") === "true";

      const doSeekAndPlay = function() {
        if (savedTime > 0 && savedTime < audio.duration) {
          audio.currentTime = savedTime;
        }
        if (wasMiniPlaying) {
          const p = audio.play();
          if (p && typeof p.then === "function") {
            p.then(() => { playBtn.textContent = "⏸"; }).catch(() => {});
          }
        }
      };
      if (audio.readyState >= 1) {
        doSeekAndPlay();
      } else {
        audio.addEventListener("loadedmetadata", function onMeta() {
          audio.removeEventListener("loadedmetadata", onMeta);
          doSeekAndPlay();
        });
      }
      sessionStorage.removeItem("miniPlayer_resumeFromMini");
    }
  } catch(e) { console.warn("[playMusic] miniPlayer restore error:", e); }

  /* ── 状態を sessionStorage に書き込むヘルパー ── */
  function saveMiniState(setActive) {
    try {
      const sourceEl = audio.querySelector("source");
      const src = (sourceEl ? sourceEl.src : "") || audio.currentSrc || audio.src || "";
      if (!src) {
        console.warn("[playMusic] saveMiniState: src が空のためスキップ");
        return;
      }
      if (setActive) sessionStorage.setItem("miniPlayer_active", "true");
      sessionStorage.setItem("miniPlayer_src",         src);
      sessionStorage.setItem("miniPlayer_title",       (playerContainer && playerContainer.dataset.title)     || "");
      sessionStorage.setItem("miniPlayer_artist",      (playerContainer && playerContainer.dataset.artist)    || "");
      sessionStorage.setItem("miniPlayer_jacket",      (playerContainer && playerContainer.dataset.jacket)    || "");
      sessionStorage.setItem("miniPlayer_currentTime", String(audio.currentTime));
      sessionStorage.setItem("miniPlayer_playing",     audio.paused ? "false" : "true");
      sessionStorage.setItem("miniPlayer_returnUrl",   (playerContainer && playerContainer.dataset.returnUrl) || location.href);
      console.log("[playMusic] saveMiniState OK – active:", setActive, "src:", src.substring(0,60), "playing:", !audio.paused);
    } catch(e) { console.warn("[playMusic] saveMiniState error:", e); }
  }

  /* ── 再生開始 / 停止時：全データ保存 ── */
  audio.addEventListener("play",  () => { console.log("[playMusic] play event"); saveMiniState(true); });
  audio.addEventListener("pause", () => { console.log("[playMusic] pause event"); saveMiniState(true); });

  /* ── 再生中：currentTime を定期保存（約250msごとに発火） ── */
  audio.addEventListener("timeupdate", () => {
    try {
      sessionStorage.setItem("miniPlayer_currentTime", String(audio.currentTime));
      sessionStorage.setItem("miniPlayer_playing",     audio.paused ? "false" : "true");
    } catch(e) {}
  });

  /* ── ページ離脱時：最終保存（beforeunload + pagehide 両方） ── */
  function onLeave() {
    console.log("[playMusic] onLeave (beforeunload/pagehide)");
    saveMiniState(true);
  }
  window.addEventListener("beforeunload", onLeave);
  window.addEventListener("pagehide",     onLeave);

  /* 再生 / 停止 */
  playBtn.addEventListener("click", () => {
    if (audio.paused) {
      audio.play();
      playBtn.textContent = "⏸";
    } else {
      audio.pause();
      playBtn.textContent = "▶";
    }
  });

  audio.addEventListener("loadedmetadata", () => {
    progress.max = audio.duration;
    durationLabel.textContent = formatTime(audio.duration);
  });

  audio.addEventListener("timeupdate", () => {
    progress.value = audio.currentTime;
    currentLabel.textContent = formatTime(audio.currentTime);
  });

  progress.addEventListener("input", () => {
    audio.currentTime = progress.value;
  });

  function formatTime(t) {
    const m = Math.floor(t / 60);
    const s = Math.floor(t % 60).toString().padStart(2, "0");
    return `${m}:${s}`;
  }

  /* ループ */
  function applyLoopState(enabled) {
    audio.loop = enabled;
    loopBtn.classList.toggle("toggle-active", enabled);
    loopBtn.setAttribute("aria-pressed", enabled ? "true" : "false");
  }

  try {
    applyLoopState(localStorage.getItem("music_loop") === "true");
  } catch {
    applyLoopState(false);
  }

  loopBtn.addEventListener("click", () => {
    const next = !audio.loop;
    applyLoopState(next);
    try {
      localStorage.setItem("music_loop", next);
    } catch {}
  });

  /* 音量 */
  let lastVolume = volumeSlider.value;
  audio.volume = volumeSlider.value;

  const savedVolume = localStorage.getItem("music_volume");
  if (savedVolume !== null) {
    audio.volume = savedVolume;
    volumeSlider.value = savedVolume;
  }
  updateIcon(audio.volume);
  updateVolumeBar(audio.volume);

  volumeSlider.addEventListener("input", () => {
    const v = Number(volumeSlider.value);
    audio.volume = v;
    if (v > 0) lastVolume = v;
    localStorage.setItem("music_volume", v);
    updateIcon(v);
    updateVolumeBar(v);
  });

  volumeIcon.addEventListener("click", () => {
    if (audio.volume > 0) {
      lastVolume = audio.volume;
      audio.volume = 0;
      volumeSlider.value = 0;
    } else {
      audio.volume = lastVolume;
      volumeSlider.value = lastVolume;
    }
    localStorage.setItem("music_volume", audio.volume);
    updateIcon(audio.volume);
    updateVolumeBar(audio.volume);
  });

  function updateIcon(v) {
    if (v === 0) volumeIcon.textContent = "🔇";
    else if (v < 0.3) volumeIcon.textContent = "🔈";
    else if (v < 0.6) volumeIcon.textContent = "🔉";
    else volumeIcon.textContent = "🔊";
  }

  function updateVolumeBar(value) {
    const min = Number(volumeSlider.min) || 0;
    const max = Number(volumeSlider.max) || 1;
    const percent = ((value - min) / (max - min)) * 100;

    volumeSlider.style.background =
      `linear-gradient(to right, white ${percent}%, rgba(255,255,255,0.4) ${percent}%)`;
  }


  /* イコライザー */
  const ctx = canvas.getContext("2d");

  function resizeCanvas() {
    canvas.width = canvas.offsetWidth;
    canvas.height = canvas.offsetHeight;
  }
  resizeCanvas();
  window.addEventListener("resize", resizeCanvas);

  const AudioContext = window.AudioContext || window.webkitAudioContext;
  const audioCtx = new AudioContext();
  const analyser = audioCtx.createAnalyser();
  analyser.fftSize = 128;

  const dataArray = new Uint8Array(analyser.frequencyBinCount);
  let sourceCreated = false;
  let isDrawing = false;

  function setupAudio() {
    if (!sourceCreated) {
      const source = audioCtx.createMediaElementSource(audio);
      source.connect(analyser);
      analyser.connect(audioCtx.destination);
      sourceCreated = true;
    }
  }

  function draw() {
    if (!isDrawing) return;

    analyser.getByteFrequencyData(dataArray);
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.fillStyle = "rgba(255,255,255,0.3)";

    const barWidth = 12;
    const gap = 8;
    const unit = barWidth + gap;
    const count = Math.min(Math.floor(canvas.width / unit), dataArray.length);
    const offsetX = (canvas.width - (count * unit - gap)) / 2;

    for (let i = 0; i < count; i++) {
      const value = dataArray[i];
      const boost = Math.sin((i / (count - 1)) * Math.PI);
      const h = (value / 255) * canvas.height * (0.3 + boost);

      ctx.beginPath();
      ctx.roundRect(
        offsetX + i * unit,
        canvas.height - h,
        barWidth,
        h,
        6
      );
      ctx.fill();
    }
    requestAnimationFrame(draw);
  }

  audio.addEventListener("play", () => {
    audioCtx.resume();
    setupAudio();
    isDrawing = true;
    draw();
  });

  audio.addEventListener("pause", () => {
    isDrawing = false;
  });

  /* いいね */
  const likeBtn = document.getElementById("likeBtn");
  const likeForm = document.getElementById("likeForm");
  const likeCount = document.getElementById("likeCount");

  if (likeBtn && likeForm && likeCount) {
      likeBtn.addEventListener("click", async (e) => {
          likeBtn.disabled = true;
          const prevCount = likeCount.textContent;
          likeCount.textContent = "...";

          try {
              const res = await fetch(likeForm.action, {
                  method: "POST",
                  headers: { "X-Requested-With": "XMLHttpRequest" },
                  body: new URLSearchParams(new FormData(likeForm)),
                  credentials: "same-origin"
              });

              if (!res.ok) throw new Error("HTTP " + res.status);
              const json = await res.json();
              if (!json || json.success !== true) throw new Error("BAD_RESPONSE");

              if (typeof json.likes === "number") {
                  likeCount.textContent = json.likes;
              } else {
                  likeCount.textContent = prevCount;
              }
          } catch (err) {
              likeCount.textContent = prevCount;
              alert("いいねの更新に失敗しました");
              console.error(err);
          } finally {
              likeBtn.disabled = false;
          }
      });
  }


  /* タイトルアニメーション */
  const title = document.querySelector(".title");
  const titleText = document.querySelector(".title-text");
  if (title && titleText) {
    applyMarquee(title, titleText, {
      speed: 70,         // px/秒
      minDuration: 4,    // 最低4秒
      maxDuration: 12,   // 最大12秒
      pauseRate: 0.2     // 停止時間比率（アニメ全体の10%停止）
    });
  }

  /* アーティスト名アニメーション */
  const artist = document.querySelector(".artist");
  const artistText = document.querySelector(".artist-text");
  if (artist && artistText) {
    applyMarquee(artist, artistText, {
      speed: 70,         // px/秒
      minDuration: 4,	 // 最低4秒
      maxDuration: 12,	 // 最大12秒
      pauseRate: 0.2 	 // 停止時間比率（アニメ全体の10%停止）
    });
  }
});
  
/* 汎用マルキュー関数 */
function applyMarquee(container, text, options) {
  const overflow = text.scrollWidth - container.clientWidth;

  // 短い場合は適用しない（ここは固定）
  if (overflow <= 0) return;

  const speed = options.speed ?? 70;
  let duration = overflow / speed;

  const minDuration = options.minDuration ?? 4;
  const maxDuration = options.maxDuration ?? 12;
  duration = Math.max(minDuration, Math.min(maxDuration, duration));

  // ここで「停止時間」を追加したい場合は pauseRate を使う
  // 例：pauseRate=0.1 => 10%停止、90%移動
  const pauseRate = options.pauseRate ?? 0.1;
  const moveRate = 1 - pauseRate;

  // CSS変数に渡す
  text.classList.add("marquee");
  text.style.setProperty("--overflow", `${overflow}px`);
  text.style.setProperty("--duration", `${duration}s`);
  text.style.setProperty("--pauseRate", `${pauseRate}`);
  text.style.setProperty("--moveRate", `${moveRate}`);
}



