/**
 * miniPlayer.js
 * ─────────────────────────────────────────────
 * playMusic ページ以外で、再生中だった曲をミニプレイヤーとして
 * 右下に表示し、再生を継続する。
 */
(function () {
  "use strict";

  console.log("[miniPlayer] スクリプト読み込み");

  /* ── sessionStorage 取得 ── */
  var ss;
  try { ss = window.sessionStorage; } catch(e) { console.warn("[miniPlayer] sessionStorage 使用不可"); return; }
  if (!ss) { console.warn("[miniPlayer] sessionStorage が null"); return; }

  var active = ss.getItem("miniPlayer_active");
  var src    = ss.getItem("miniPlayer_src");
  console.log("[miniPlayer] active =", active, "| src =", src ? src.substring(0,60) : "(なし)");

  if (active !== "true") { console.log("[miniPlayer] active が true でないため終了"); return; }
  if (!src)              { console.log("[miniPlayer] src が空のため終了"); return; }

  /* ── 値の取得 ── */
  var title      = ss.getItem("miniPlayer_title")  || "不明な曲";
  var artist     = ss.getItem("miniPlayer_artist") || "不明なアーティスト";
  var jacket     = ss.getItem("miniPlayer_jacket") || "";
  var savedTime  = parseFloat(ss.getItem("miniPlayer_currentTime")) || 0;
  var wasPlaying = ss.getItem("miniPlayer_playing") === "true";
  var returnUrl  = ss.getItem("miniPlayer_returnUrl") || "";

  console.log("[miniPlayer] title:", title, "| artist:", artist, "| time:", savedTime, "| wasPlaying:", wasPlaying);

  /* 音量 */
  var savedVolume = null;
  try { savedVolume = localStorage.getItem("music_volume"); } catch(e) {}

  /* ── コンテキストパス ── */
  function getCtxPath() {
    var meta = document.querySelector("meta[name='ctx-path']");
    return meta ? meta.content : "";
  }

  /* ── DOM 構築 ── */
  function build() {
    var wrap = document.createElement("div");
    wrap.id = "mini-player";
    wrap.innerHTML =
      '<button class="mp-btn mp-btn-close" title="\u9589\u3058\u308B" type="button">\u2715</button>' +
      '<div class="mp-jacket"><img src="" alt="\u30B8\u30E3\u30B1\u30C3\u30C8"></div>' +
      '<div class="mp-info">' +
        '<div class="mp-title"></div>' +
        '<div class="mp-artist"></div>' +
      '</div>' +
      '<div class="mp-controls">' +
        '<button class="mp-btn mp-btn-play" title="\u518D\u751F / \u4E00\u6642\u505C\u6B62" type="button">\u25B6</button>' +
        '<button class="mp-btn mp-btn-return" title="\u518D\u751F\u753B\u9762\u306B\u623B\u308B" type="button">\u23CF</button>' +
      '</div>' +
      '<div class="mp-progress-wrap"><div class="mp-progress-bar"></div></div>';

    wrap.querySelector(".mp-title").textContent  = title;
    wrap.querySelector(".mp-artist").textContent = artist;

    var img = wrap.querySelector(".mp-jacket img");
    var fallbackSrc = getCtxPath() + "/png/MusiConLogo.png";
    if (jacket) {
      img.src = jacket;
      img.onerror = function () { this.onerror = null; this.src = fallbackSrc; };
    } else {
      img.src = fallbackSrc;
    }

    document.body.appendChild(wrap);
    console.log("[miniPlayer] DOM 構築完了、body に追加");
    return wrap;
  }

  /* ── 初期化 ── */
  function init() {
    console.log("[miniPlayer] init() 開始");

    var el          = build();
    var playBtn     = el.querySelector(".mp-btn-play");
    var returnBtn   = el.querySelector(".mp-btn-return");
    var closeBtn    = el.querySelector(".mp-btn-close");
    var progressBar = el.querySelector(".mp-progress-bar");

    /* ── オーディオ要素 ── */
    var audio = document.createElement("audio");
    audio.preload = "auto";
    if (savedVolume !== null) audio.volume = Math.min(1, Math.max(0, parseFloat(savedVolume)));
    else audio.volume = 0.7;

    var seekDone = false;

    function doSeekAndPlay() {
      if (seekDone) return;
      seekDone = true;
      console.log("[miniPlayer] doSeekAndPlay – duration:", audio.duration, "savedTime:", savedTime);
      try {
        if (savedTime > 0 && isFinite(audio.duration) && savedTime < audio.duration) {
          audio.currentTime = savedTime;
        }
      } catch(e) { console.warn("[miniPlayer] seek error:", e); }
      if (wasPlaying) {
        var p = audio.play();
        if (p && typeof p.then === "function") {
          p.then(function () {
            playBtn.textContent = "\u23F8";
            console.log("[miniPlayer] 再生開始成功");
          }).catch(function (err) {
            playBtn.textContent = "\u25B6";
            console.warn("[miniPlayer] 再生開始失敗:", err);
          });
        }
      }
    }

    audio.addEventListener("loadedmetadata", function () {
      console.log("[miniPlayer] loadedmetadata – duration:", audio.duration);
      doSeekAndPlay();
    });
    audio.addEventListener("canplay", function () {
      console.log("[miniPlayer] canplay");
      doSeekAndPlay();
    });
    audio.addEventListener("error", function () {
      console.error("[miniPlayer] audio error:", audio.error);
      playBtn.textContent = "\u25B6";
    });

    /* ソース設定・ロード開始 */
    console.log("[miniPlayer] audio.src を設定:", src.substring(0, 80));
    audio.src = src;
    audio.load();

    if (audio.readyState >= 1) {
      console.log("[miniPlayer] readyState already >=1, calling doSeekAndPlay");
      doSeekAndPlay();
    }

    /* ── 再生 / 停止ボタン ── */
    playBtn.addEventListener("click", function () {
      if (audio.paused) {
        var p = audio.play();
        if (p && typeof p.then === "function") {
          p.then(function () { playBtn.textContent = "\u23F8"; }).catch(function () {});
        }
      } else {
        audio.pause();
        playBtn.textContent = "\u25B6";
      }
    });

    /* ── プログレスバー ── */
    audio.addEventListener("timeupdate", function () {
      if (audio.duration && isFinite(audio.duration)) {
        progressBar.style.width = ((audio.currentTime / audio.duration) * 100) + "%";
      }
      try {
        ss.setItem("miniPlayer_currentTime", String(audio.currentTime));
        ss.setItem("miniPlayer_playing",     audio.paused ? "false" : "true");
      } catch(e) {}
    });

    /* ── 再生画面に戻るボタン ── */
    returnBtn.addEventListener("click", function () {
      console.log("[miniPlayer] 戻るボタン押下 → returnUrl:", returnUrl);
      try {
        ss.setItem("miniPlayer_currentTime",    String(audio.currentTime));
        ss.setItem("miniPlayer_playing",         audio.paused ? "false" : "true");
        ss.setItem("miniPlayer_resumeFromMini", "true");
      } catch(e) {}
      audio.pause();
      if (returnUrl) {
        var sep = returnUrl.indexOf("?") >= 0 ? "&" : "?";
        window.location.href = returnUrl + sep + "autoplay=true";
      }
    });

    /* ── 閉じるボタン ── */
    closeBtn.addEventListener("click", function () {
      audio.pause();
      try { audio.src = ""; } catch(e) {}
      clearState();
      el.classList.add("hiding");
      setTimeout(function () { el.remove(); }, 350);
    });

    /* ── 曲が終了したとき ── */
    audio.addEventListener("ended", function () {
      playBtn.textContent = "\u25B6";
      progressBar.style.width = "100%";
      try { ss.setItem("miniPlayer_playing", "false"); } catch(e) {}
    });

    /* ── ページ遷移時に最終保存 ── */
    function onLeave() {
      try {
        ss.setItem("miniPlayer_currentTime", String(audio.currentTime));
        ss.setItem("miniPlayer_playing",     audio.paused ? "false" : "true");
      } catch(e) {}
    }
    window.addEventListener("beforeunload", onLeave);
    window.addEventListener("pagehide",     onLeave);
  }

  /* ── 状態クリア ── */
  function clearState() {
    var keys = [
      "miniPlayer_active", "miniPlayer_src", "miniPlayer_title",
      "miniPlayer_artist", "miniPlayer_jacket", "miniPlayer_currentTime",
      "miniPlayer_playing", "miniPlayer_returnUrl", "miniPlayer_resumeFromMini"
    ];
    for (var i = 0; i < keys.length; i++) {
      try { ss.removeItem(keys[i]); } catch(e) {}
    }
  }

  /* ── 起動 ── */
  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();
