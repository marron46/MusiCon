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
String userName = (String) session.getAttribute("user_name");
if (userName == null) userName = "";

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
String jacketUrl = "";
if (musicUrl != null) {
    if (musicUrl.contains("MusicFile?file=")) {
        if (!musicUrl.startsWith("http") && !musicUrl.startsWith(request.getContextPath())) {
            if (musicUrl.startsWith("/")) musicUrl = request.getContextPath() + musicUrl;
            else musicUrl = request.getContextPath() + "/" + musicUrl;
        }
        int fileStart = musicUrl.indexOf("file=");
        if (fileStart >= 0) {
            String fileParam = musicUrl.substring(fileStart + 5);
            if (fileParam.contains("&")) fileParam = fileParam.substring(0, fileParam.indexOf("&"));
            if (!fileParam.isEmpty()) {
                jacketUrl = request.getContextPath() + "/Jacket?file=" + java.net.URLEncoder.encode(fileParam, "UTF-8");
            }
        }
    } else if (musicUrl.contains("/music/")) {
        String fileName = musicUrl.substring(musicUrl.lastIndexOf("/") + 1);
        musicUrl = request.getContextPath() + "/MusicFile?file=" + java.net.URLEncoder.encode(fileName, "UTF-8");
        jacketUrl = request.getContextPath() + "/Jacket?file=" + java.net.URLEncoder.encode(fileName, "UTF-8");
    } else if (!musicUrl.startsWith("http") && !musicUrl.startsWith("/")) {
        musicUrl = request.getContextPath() + "/" + musicUrl;
    } else if (musicUrl.startsWith("/") && !musicUrl.startsWith(request.getContextPath())) {
        musicUrl = request.getContextPath() + musicUrl;
    }
}
%>

<div class="reverse">
    <a href="<%=isPlaylistMode ? request.getContextPath() + "/MyPlaylist" : request.getContextPath() + "/PlayMusic"%>">
        <img src="${pageContext.request.contextPath}/png/MusiConLogo.png" alt="ロゴ" class="reverse-img">
    </a>
</div>

<input type="checkbox" id="menu-check" class="menu-check">
<label for="menu-check" class="hamburger">
    <div class="line"></div>
    <div class="line"></div>
    <div class="line"></div>
</label>
<div class="overlay"></div>
<nav class="side-menu">
    <ul class="user-profile">
        <img src="${pageContext.request.contextPath}/png/musi_usericon.png" class="useri" width="36" alt="ユーザーアイコン">
        <li><%=userName%>さん</li>
    </ul>
    <ul class="menu-list">
        <li><a href="${pageContext.request.contextPath}/PlayMusic" class="menu">TOP / TOPに戻る</a></li>
        <li><a href="${pageContext.request.contextPath}/MyPlaylist" class="menu">Playlist / プレイリスト</a></li>
        <li><a href="${pageContext.request.contextPath}/ShowRanking" class="menu">Ranking / ランキング</a></li>
        <li><a href="${pageContext.request.contextPath}/jsp/importMusic.jsp" class="menu">Add music / 曲アップロード</a></li>
        <li><a href="${pageContext.request.contextPath}/MusicList" class="menu">Music list / 楽曲一覧</a></li>
    </ul>
    <ul class="menu-bottom">
        <li><a href="${pageContext.request.contextPath}/Logout" class="menu logout">Log out / ログアウト</a></li>
        <li><a href="${pageContext.request.contextPath}/jsp/deleteUser.jsp" class="menu delete">Delete account / アカウント削除</a></li>
    </ul>
</nav>
<script>
document.querySelector(".overlay").addEventListener("click", () => {
    document.getElementById("menu-check").checked = false;
});
</script>

<% String defaultJacketUrl = request.getContextPath() + "/png/MusiConLogo.png"; %>
<div id="player-container">
    <div class="album-art">
        <img src="<%= (jacketUrl != null && !jacketUrl.isEmpty()) ? jacketUrl : defaultJacketUrl %>" alt="ジャケット" class="album-art-img"
             onerror="this.onerror=null; this.src='<%= defaultJacketUrl %>';">
    </div>
    <div class="info">
    
		<!-- タイトル -->
        <h2 class="title"><span class="title-text"><%=music.getTitle()%></span></h2>
        
        <!-- アーティスト名 -->
        <p class="artist"><span class="artist-text"><%=music.getArtist()%></span></p>

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

            <div class="like-bookmark-box">
                <form id="likeForm" action="${pageContext.request.contextPath}/LikeMusic" method="post">
                    <input type="hidden" name="id" value="<%=music.getId()%>">
                    <button id="likeBtn" type="button" class="like-btn">
                        いいね！ (<span id="likeCount"><%=music.getLikes()%></span>)
                    </button>
                </form>

                <form id="playlistForm" action="${pageContext.request.contextPath}/MyPlaylist" method="post">
                    <input type="hidden" name="id" value="<%=music.getId()%>">
                    <button id="playlistBtn" type="button" class="like-btn <%= isInPlaylist ? "playlist-in" : "playlist-out" %>">
                        <%=isInPlaylist ? "★ プレイリストから外す" : "☆ プレイリストに追加"%>
                    </button>
                </form>
            </div>
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

    

    if(playlistBtn && playlistForm){
        playlistBtn.addEventListener("click", async e=>{
            e.preventDefault();
            playlistBtn.disabled = true;
            const prevText = playlistBtn.textContent;
            const prevClass = playlistBtn.classList.contains("playlist-in") ? "playlist-in" : "playlist-out";
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
                playlistBtn.classList.remove("playlist-in","playlist-out");
                playlistBtn.classList.add(inPlaylist ? "playlist-in" : "playlist-out");
                playlistBtn.textContent = inPlaylist ? "★ プレイリストから外す" : "☆ プレイリストに追加";
            }catch(e){playlistBtn.textContent=prevText; playlistBtn.classList.remove("playlist-in","playlist-out"); playlistBtn.classList.add(prevClass);}
            finally{playlistBtn.disabled=false;}
        });
    }
});
</script>

</body>
</html>
