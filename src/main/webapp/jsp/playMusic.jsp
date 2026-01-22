<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.Music"%>
<%@ page import="model.User"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>曲の再生</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/playMusic.css">
<script defer src="${pageContext.request.contextPath}/js/playMusic.js"></script>
</head>
<body>

<%
Music music = (Music) request.getAttribute("music");
Boolean isInPlaylist = (Boolean) request.getAttribute("isInPlaylist");
Boolean isPlaylistMode = (Boolean) request.getAttribute("isPlaylistMode");
Integer playlistPos = (Integer) request.getAttribute("playlistPos");
Integer playlistSize = (Integer) request.getAttribute("playlistSize");
if (isInPlaylist == null) isInPlaylist = false;
if (isPlaylistMode == null) isPlaylistMode = false;
if (playlistPos == null) playlistPos = 0;
if (playlistSize == null) playlistSize = 0;

if (music == null) {
    if (isPlaylistMode) {
        response.sendRedirect(request.getContextPath() + "/MyPlaylist");
    } else {
        response.sendRedirect(request.getContextPath() + "/PlayMusic");
    }
    return;
}

String nextUrl;
String prevUrl;
if (isPlaylistMode) {
    nextUrl = request.getContextPath() + "/PlayMusic?playlistMode=true&action=next";
    prevUrl = request.getContextPath() + "/PlayMusic?playlistMode=true&action=prev";
} else {
    nextUrl = request.getContextPath() + "/PlayMusic?next=" + music.getId();
    prevUrl = request.getContextPath() + "/PlayMusic?prev=" + music.getId();
}
boolean autoPlay = "true".equals(request.getParameter("autoplay"));

String musicUrl = music.getUrl();
if (musicUrl != null) {
    if (musicUrl.contains("MusicFile?file=")) {
        if (!musicUrl.startsWith("http") && !musicUrl.startsWith(request.getContextPath())) {
            if (musicUrl.startsWith("/")) musicUrl = request.getContextPath() + musicUrl;
            else musicUrl = request.getContextPath() + "/" + musicUrl;
        }
    } else if (musicUrl.contains("/music/")) {
        String fileName = musicUrl.substring(musicUrl.lastIndexOf("/") + 1);
        musicUrl = request.getContextPath() + "/MusicFile?file=" + java.net.URLEncoder.encode(fileName, "UTF-8");
    } else if (!musicUrl.startsWith("http") && !musicUrl.startsWith("/")) {
        musicUrl = request.getContextPath() + "/" + musicUrl;
    } else if (musicUrl.startsWith("/") && !musicUrl.startsWith(request.getContextPath())) {
        musicUrl = request.getContextPath() + musicUrl;
    }
}
%>

<div class="reverse">
    <a href="<%=isPlaylistMode ? request.getContextPath() + "/MyPlaylist" : request.getContextPath() + "/PlayMusic"%>">
        <img src="${pageContext.request.contextPath}/png/MusiConLogo.png" alt="TOPに戻る" class="reverse-img">
    </a>
</div>
<div class="reverseStr">
    <a href="<%=isPlaylistMode ? request.getContextPath() + "/MyPlaylist" : request.getContextPath() + "/PlayMusic"%>">
        <%=isPlaylistMode ? "プレイリストへ戻る" : "TOPに戻る"%>
    </a>
</div>

<div id="player-container">
    <div class="album-art"></div>
    <div class="info">
        <h2 class="title"><span class="title-text"><%=music.getTitle()%></span></h2>
        <p class="artist"><%=music.getArtist()%></p>

        <audio id="audio" preload="metadata">
            <source src="<%=musicUrl%>" type="audio/mpeg">
            <p>お使いのブラウザは音声再生に対応していません。</p>
        </audio>

        <div class="center-block">
            <div class="controls">
                <button id="prev" type="button">⏮</button>
                <button id="play" class="play" type="button">▶</button>
                <button id="next" type="button">⏭</button>
                <button id="loop" class="toggle" aria-pressed="false" title="ループ（1曲リピート）" type="button">↩</button>
            </div>
            <div class="controls2"></div>

            <canvas id="equalizer" color="white"></canvas>

            <div class="progress-area">
                <span id="current">0:00</span>
                <input type="range" id="progress" min="0" value="0">
                <span id="duration">0:00</span>
            </div>

            <div class="volume-area">
                <span id="volume-icon">🔊</span>
                <input type="range" id="volume" min="0" max="0.7" step="0.02" value="0.7">
            </div>

            <form id="likeForm" action="${pageContext.request.contextPath}/LikeMusic" method="post">
                <input type="hidden" name="id" value="<%=music.getId()%>">
                <button id="likeBtn" type="button">いいね！ (<span id="likeCount"><%=music.getLikes()%></span>)</button>
            </form>

            <form id="playlistForm" action="${pageContext.request.contextPath}/MyPlaylist" method="post">
                <input type="hidden" name="id" value="<%=music.getId()%>">
                <button id="playlistBtn" type="button"
                        style="background-color: <%=isInPlaylist ? "#f7d358" : "#dddddd"%>;">
                    <%=isInPlaylist ? "★ プレイリストから外す" : "☆ プレイリストに追加"%>
                </button>
            </form>
        </div>
    </div>
</div>

<script>
window.addEventListener("DOMContentLoaded", () => {
    const audioEl = document.getElementById("audio");
    const playBtn = document.getElementById("play");
    const nextBtn = document.getElementById("next");
    const prevBtn = document.getElementById("prev");

    if (nextBtn) nextBtn.onclick = () => window.location.href = "<%=nextUrl%>";
    if (prevBtn) prevBtn.onclick = () => window.location.href = "<%=prevUrl%>";

    if (audioEl) {
        audioEl.addEventListener("ended", () => {
            try {
                const loopEnabled = audioEl.loop || localStorage.getItem("music_loop") === "true";
                if (loopEnabled) {
                    audioEl.currentTime = 0;
                    audioEl.play?.();
                    return;
                }
            } catch(e){}
            window.location.href = "<%=nextUrl%>&autoplay=true";
        });
        if ("<%=autoPlay%>" === "true") {
            const p = audioEl.play?.();
            if (p && typeof p.then === "function") p.then(() => playBtn.textContent = "⏸").catch(()=>{});
            else try{audioEl.play?.(); playBtn.textContent="⏸"}catch(e){}
        }
    }

    const likeBtn = document.getElementById("likeBtn");
    const likeForm = document.getElementById("likeForm");
    const likeCount = document.getElementById("likeCount");

    const playlistBtn = document.getElementById("playlistBtn");
    const playlistForm = document.getElementById("playlistForm");

    if(likeBtn && likeForm && likeCount){
        likeBtn.addEventListener("click", async e=>{
            e.preventDefault();
            likeBtn.disabled = true;
            const prevCount = likeCount.textContent;
            likeCount.textContent = "...";
            try{
                const res = await fetch(likeForm.action,{
                    method:"POST",
                    headers:{"X-Requested-With":"XMLHttpRequest"},
                    body:new URLSearchParams(new FormData(likeForm)),
                    credentials:"same-origin"
                });
                const json = await res.json();
                likeCount.textContent = json?.likes ?? prevCount;
            }catch(e){likeCount.textContent = prevCount;}
            finally{likeBtn.disabled=false;}
        });
    }

    if(playlistBtn && playlistForm){
        playlistBtn.addEventListener("click", async e=>{
            e.preventDefault();
            playlistBtn.disabled = true;
            const prevText = playlistBtn.textContent;
            const prevBg = playlistBtn.style.backgroundColor;
            playlistBtn.textContent = "更新中...";
            try{
                const res = await fetch(playlistForm.action,{
                    method:"POST",
                    headers:{"X-Requested-With":"XMLHttpRequest"},
                    body:new URLSearchParams(new FormData(playlistForm)),
                    credentials:"same-origin"
                });
                const json = await res.json();
                const inPlaylist = !!json?.inPlaylist;
                playlistBtn.dataset.inPlaylist = inPlaylist;
                if(inPlaylist){playlistBtn.textContent="★ プレイリストから外す"; playlistBtn.style.backgroundColor="#f7d358";}
                else {playlistBtn.textContent="☆ プレイリストに追加"; playlistBtn.style.backgroundColor="#dddddd";}
            }catch(e){playlistBtn.textContent=prevText; playlistBtn.style.backgroundColor=prevBg;}
            finally{playlistBtn.disabled=false;}
        });
    }
});
</script>

</body>
</html>
