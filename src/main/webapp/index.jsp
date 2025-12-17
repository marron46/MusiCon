<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	// ログイン済みの場合はtop.jspにリダイレクト
	String userName = (String) session.getAttribute("user_name");
	if (userName != null) {
		response.sendRedirect(request.getContextPath() + "/PlayMusic");
		return;
	}
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8" />
  <title>MUSICON</title>

  <!-- Google Font -->
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Alfa+Slab+One&display=swap" rel="stylesheet">

  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
</head>
<body>

<!-- イコライザーの線 -->
<div id="equalizer-bg"></div>

<!-- 上のマルキー -->
<div class="marquee top">
	<div class="track" id="track-top">
		<span class="item">MUSICON</span>
	</div>
</div>

<!-- メイン -->
<main class="hero">
    <div class="left">
      <p class="catch">日常を、音楽と共に。</p>
      <h1 class="logo">MUSICON</h1>
      <a href="${pageContext.request.contextPath}/jsp/login.jsp" button class="login-btn">ログイン</button></a>
      <a href="${pageContext.request.contextPath}/jsp/createUser.jsp" class="first-link">初めての方はこちら</a>
    </div>

    <div class="right">
      <p>
        好きな音楽をプレイリストに追加して<br>
        日常を豊かにしよう。
      </p>
    </div>
  </main>

  <!-- 下のマルキー -->
  <div class="marquee bottom">
    <div class="track" id="track-bottom">
      <span class="item">MUSICON</span>
    </div>
  </div>

  <script defer src="${pageContext.request.contextPath}/js/index.js"></script>
</body>
</html>
