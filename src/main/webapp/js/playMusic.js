window.addEventListener("DOMContentLoaded", () => {

  const audio = document.getElementById("audio");
  const playBtn = document.getElementById("play");
  const loopBtn = document.getElementById("loop");
  const progress = document.getElementById("progress");
  const currentLabel = document.getElementById("current");
  const durationLabel = document.getElementById("duration");
  const volumeSlider = document.getElementById("volume");
  const volumeIcon = document.getElementById("volume-icon");
  const canvas = document.getElementById("equalizer");

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


});
