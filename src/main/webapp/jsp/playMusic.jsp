<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.Music"%>
<%@ page import="model.User"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>曲の再生</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/playMusic.css">
<script defer src="${pageContext.request.contextPath}/js/playMusic.js"></script>

</head>

<body>

	<%
	// 変数を最初に定義
	Music music = (Music) request.getAttribute("music");
	Boolean isInPlaylist = (Boolean) request.getAttribute("isInPlaylist");
	Boolean isPlaylistMode = (Boolean) request.getAttribute("isPlaylistMode");
	Integer playlistPos = (Integer) request.getAttribute("playlistPos");
	Integer playlistSize = (Integer) request.getAttribute("playlistSize");
	if (isInPlaylist == null) isInPlaylist = false;
	if (isPlaylistMode == null) isPlaylistMode = false;
	if (playlistPos == null) playlistPos = 0;
	if (playlistSize == null) playlistSize = 0;
	
	%>

	<div class="reverse">
		<a
			href="<%=isPlaylistMode ? request.getContextPath() + "/MyPlaylist" : request.getContextPath() + "/PlayMusic"%>"><img
			src="${pageContext.request.contextPath}/png/MusiConLogo.png"
			alt="TOPに戻る" class="reverse-img"> </a>
	</div>
	<div class="reverseStr">
		<a
			href="<%=isPlaylistMode ? request.getContextPath() + "/MyPlaylist" : request.getContextPath() + "/PlayMusic"%>">
			<%=isPlaylistMode ? "プレイリストへ戻る" : "TOPに戻る"%>
		</a>
	</div>

	<%
	// musicがnullの場合はエラーページにリダイレクト
		if (music == null) {
			if (isPlaylistMode) {
				response.sendRedirect(request.getContextPath() + "/MyPlaylist");
			} else {
				response.sendRedirect(request.getContextPath() + "/PlayMusic");
			}
			return;
		}
		
	%>

	<%
	String nextUrl;
	String prevUrl;
	if (isPlaylistMode) {
		// 次へ/前へは action だけ。現在位置はセッションで管理する（URLのindex依存を排除）
		nextUrl = request.getContextPath() + "/PlayMusic?playlistMode=true&action=next";
		prevUrl = request.getContextPath() + "/PlayMusic?playlistMode=true&action=prev";
	} else {
		nextUrl = request.getContextPath() + "/PlayMusic?next=" + music.getId();
		prevUrl = request.getContextPath() + "/PlayMusic?prev=" + music.getId();
	}
	boolean autoPlay = "true".equals(request.getParameter("autoplay"));
	%>
	<script>
	// DOM読み込み後にハンドラを登録（要素未生成によるnull回避＆変数重複回避）
	window.addEventListener("DOMContentLoaded", () => {
		const nextBtn = document.getElementById("next");
		const prevBtn = document.getElementById("prev");
		const audioEl = document.getElementById("audio");
		const playBtn = document.getElementById("play");
		if (nextBtn) {
			nextBtn.onclick = () => window.location.href = "<%=nextUrl%>";
		}
		if (prevBtn) {
			prevBtn.onclick = () => window.location.href = "<%=prevUrl%>";
		}
		if (audioEl) {
			audioEl.addEventListener("ended", () => {
				// ループONなら次曲へ遷移しない（localStorageで状態保持）
				try {
					const loopEnabled = audioEl.loop || localStorage.getItem("music_loop") === "true";
					if (loopEnabled) {
						audioEl.currentTime = 0;
						audioEl.play?.();
						return;
					}
				} catch (e) {
					// localStorageが使えない等は無視して通常動作
				}
				// 次曲へ遷移し、次ページ側で自動再生を試みる
				window.location.href = "<%=nextUrl%>&autoplay=true";
			});
		}

		// 次曲ページでの自動再生（ブラウザの自動再生制限で失敗する場合あり）
		const shouldAutoPlay = "<%=autoPlay%>" === "true";
		if (audioEl && shouldAutoPlay) {
			const p = audioEl.play?.();
			if (p && typeof p.then === "function") {
				p.then(() => {
					if (playBtn) playBtn.textContent = "⏸";
				}).catch(() => {
					// 自動再生がブロックされた場合は何もしない（ユーザーが▶を押せばOK）
				});
			} else {
				// 古いブラウザ向け
				try {
					audioEl.play?.();
					if (playBtn) playBtn.textContent = "⏸";
				} catch (e) {}
			}
		}
	});
	</script>

	<!-- ▼ 追加：中央配置用のラッパー ▼ -->
	<div id="player-container">
		<!--<div class="center-wrapper">
		<div class="player">-->

		<!-- 左のジャケット（白無地） -->
		<div class="album-art"></div>

		<!-- 右側情報 -->
		<div class="info">

			<!-- 曲タイトル -->
			<h2 class="title"><%=music.getTitle()%></h2>

			<!-- アーティスト名 -->
			<p class="artist"><%=music.getArtist()%></p>

			<!-- 再生する audio -->
			<audio id="audio" preload="metadata"
				<!-- crossorigin="anonymous" -->
				>

				<%
					String musicUrl = music.getUrl();
					if (musicUrl != null) {
						// 既にサーブレット経由のURL（MusicFile?file=）の場合はそのまま使用
						if (musicUrl.contains("MusicFile?file=")) {
							// サーブレット経由のURLはそのまま使用
							// コンテキストパスが含まれていない場合は追加
							if (!musicUrl.startsWith("http") && !musicUrl.startsWith(request.getContextPath())) {
								if (musicUrl.startsWith("/")) {
									musicUrl = request.getContextPath() + musicUrl;
								} else {
									musicUrl = request.getContextPath() + "/" + musicUrl;
								}
							}
						}
						// 古い形式のURL（/music/ファイル名.mp3）の場合はサーブレット経由のURLに変換
						// 既存データとの互換性のため、URLエンコードを実行
						else if (musicUrl.contains("/music/")) {
							// ファイル名を抽出
							String fileName = musicUrl.substring(musicUrl.lastIndexOf("/") + 1);
							// サーブレット経由のURLに変換（既存データのためURLエンコード）
							musicUrl = request.getContextPath() + "/MusicFile?file=" + java.net.URLEncoder.encode(fileName, "UTF-8");
						}
						// その他の相対パスの場合
						else if (!musicUrl.startsWith("http") && !musicUrl.startsWith("/")) {
							musicUrl = request.getContextPath() + "/" + musicUrl;
						} else if (musicUrl.startsWith("/") && !musicUrl.startsWith(request.getContextPath())) {
							musicUrl = request.getContextPath() + musicUrl;
						}
					}
					%>
				<source src="<%=musicUrl%>" type="audio/mpeg">
				<p>お使いのブラウザは音声再生に対応していません。</p>
			</audio>

			<div class="center-block">
				<!-- 再生ボタン -->
				<div class="controls">
					<button id="prev">⏮</button>
					<button id="play" class="play">▶</button>
					<button id="next">⏭</button>
					<button id="loop" class="toggle" aria-pressed="false"
						title="ループ（1曲リピート）">↩</button>
				</div>
				<div class="controls2"></div>

				<!-- イコライザー -->
				<canvas id="equalizer" color="white"></canvas>

				<!-- シークバー -->
				<div class="progress-area">
					<span id="current">0:00</span> <input type="range" id="progress"
						min="0" value="0"> <span id="duration">0:00</span>
				</div>


				<!--  音量バー  -->
				<div class="volume-area">
					<span id="volume-icon">🔊</span> <input type="range" id="volume"
						min="0" max="0.7" step="0.02" value="0.7">
				</div>

			</div>

			<div class="like-bookmark-box">
				<!-- いいね -->
				<form id="likeForm"
					action="${pageContext.request.contextPath}/LikeMusic" method="post">
					<input type="hidden" name="id" value="<%=music.getId()%>">
					<input type="hidden" name="ajax" value="true">
					<button id="likeBtn" type="submit" class="like-btn">
						いいね！ (<span id="likeCount"><%=music.getLikes()%></span>)
					</button>
					
				</form>

				<!-- ブックマーク -->
				<form id="playlistForm"
					action="${pageContext.request.contextPath}/MyPlaylist"
					method="post">
					<input type="hidden" name="id" value="<%=music.getId()%>">
					<input type="hidden" name="ajax" value="true">
					<%
					if (isPlaylistMode) {
					%>
					<input type="hidden" name="playlistMode" value="true"> <input
						type="hidden" name="playlistPos" value="<%=playlistPos%>">
					<%
					}
					%>
					<%
					if (isInPlaylist) {
					%>
					<button id="playlistBtn" type="submit" class="like-btn"
						data-in-playlist="true" style="background-color: #f7d358;">★
						プレイリストから外す</button>
					<%
					} else {
					%>
					<button id="playlistBtn" type="submit" class="like-btn"
						data-in-playlist="false" style="background-color: #dddddd;">☆
						プレイリストに追加</button>
					<%
					}
					%>
				</form>
			</div>

			<script>
				// いいね：画面遷移させずに DB だけ更新（AJAX）
				window.addEventListener("DOMContentLoaded", () => {
					const likeForm = document.getElementById("likeForm");
					const likeBtn = document.getElementById("likeBtn");
					const likeCount = document.getElementById("likeCount");
					if (!likeForm || !likeBtn || !likeCount) return;

					likeForm.addEventListener("submit", async (e) => {
						e.preventDefault();
						likeBtn.disabled = true;
						const prevCount = likeCount.textContent;
						likeCount.textContent = "...";

						try {
							const res = await fetch(likeForm.action, {
								method: "POST",
								headers: { "X-Requested-With": "XMLHttpRequest" },
								// FormDataを直接送ると multipart になり getParameter で取れない場合があるため urlencoded にする
								body: new URLSearchParams(new FormData(likeForm)),
								credentials: "same-origin",
							});
							if (!res.ok) throw new Error("HTTP " + res.status);
							const json = await res.json();
							if (!json || json.success !== true) throw new Error("BAD_RESPONSE");

							if (typeof json.likes === "number" && json.likes >= 0) {
								likeCount.textContent = String(json.likes);
							} else {
								likeCount.textContent = prevCount;
							}
						} catch (err) {
							likeCount.textContent = prevCount;
							alert("いいねの更新に失敗しました。もう一度お試しください。");
							console.error(err);
						} finally {
							likeBtn.disabled = false;
						}
					});
				});

				// プレイリスト登録/解除：画面遷移させずに DB だけ更新（AJAX）
				window.addEventListener("DOMContentLoaded", () => {
					const form = document.getElementById("playlistForm");
					const btn = document.getElementById("playlistBtn");
					if (!form || !btn) return;

					form.addEventListener("submit", async (e) => {
						e.preventDefault();
						btn.disabled = true;

						const prevText = btn.textContent;
						const prevBg = btn.style.backgroundColor;
						btn.textContent = "更新中...";

						try {
							const res = await fetch(form.action, {
								method: "POST",
								headers: { "X-Requested-With": "XMLHttpRequest" },
								// FormDataを直接送ると multipart になり getParameter で取れない場合があるため urlencoded にする
								body: new URLSearchParams(new FormData(form)),
								credentials: "same-origin",
							});
							if (!res.ok) throw new Error("HTTP " + res.status);

							// JSON以外が返るケースもあるので安全にparseする
							const raw = await res.text();
							let json;
							try {
								json = JSON.parse(raw);
							} catch (parseErr) {
								throw new Error("NON_JSON_RESPONSE: " + raw.slice(0, 120));
							}
							if (!json || json.success !== true) throw new Error("BAD_RESPONSE");

							const inPlaylist = !!json.inPlaylist;
							btn.dataset.inPlaylist = String(inPlaylist);
							if (inPlaylist) {
								btn.textContent = "★ プレイリストから外す";
								btn.style.backgroundColor = "#f7d358";
							} else {
								btn.textContent = "☆ プレイリストに追加";
								btn.style.backgroundColor = "#dddddd";
							}
						} catch (err) {
							btn.textContent = prevText;
							btn.style.backgroundColor = prevBg;
							alert("プレイリスト更新に失敗しました。もう一度お試しください。");
							console.error(err);
						} finally {
							btn.disabled = false;
						}
					});
				});
				</script>

		</div>

	</div>

	</div>

</body>
</html>