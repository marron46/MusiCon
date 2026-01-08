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

<!-- Scroll＞を表示 -->
<div class="scroll-hint">
  Scroll down <span class="arrow">&gt;</span>
</div>


<!-- メイン -->
<main class="hero">
    <div class="left">
      <p class="catch">日常を、音楽と共に。</p>
      <h1 class="logo">MUSICON</h1>
      <a href="${pageContext.request.contextPath}/jsp/login.jsp" class="login-btn">ログイン</a>
      <a href="${pageContext.request.contextPath}/jsp/createUser.jsp" class="first-link">初めての方はこちら</a>
    </div>

    <div class="right">
      <div class="scroll-content">
        <h1 class="info">About "MusiCon"</h1>
        <p>　MusiConはあなたの生活の一部となり、毎日をさらに豊かなものにします。</p>
        <p>　再生できる音楽は∞曲。追加すればするほど新しい音楽に出会えます。</p>
        <br>
        <h1 class="info">Why "MusiCon"</h1>
        <p>　音楽である『Music』　×　音楽記号である『con』</p>
        <p>　『con』はイタリア語で"～と共に"という意味になる。</p>
        <p>　この2つをかけ合わせ「音楽と共に」というコンセプトを重視しています。</p>
      </div>
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
