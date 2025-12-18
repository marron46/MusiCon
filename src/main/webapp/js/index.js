/* マルキー制御 */
function setupMarquee(track) {
  const baseItem = track.children[0];

  // 画面幅×2を超えるまで複製
  while (track.offsetWidth < window.innerWidth * 2) {
    track.appendChild(baseItem.cloneNode(true));
  }

  // 無限スクロール用にもう一度複製
  const items = Array.from(track.children);
  items.forEach(item => {
    track.appendChild(item.cloneNode(true));
  });
}

const topTrack = document.getElementById('track-top');
const bottomTrack = document.getElementById('track-bottom');

setupMarquee(topTrack);
setupMarquee(bottomTrack);

// リサイズ対応
window.addEventListener('resize', () => {
  topTrack.innerHTML = '<span class="item">MUSICON</span>';
  bottomTrack.innerHTML = '<span class="item">MUSICON</span>';
  setupMarquee(topTrack);
  setupMarquee(bottomTrack);
});



/* 背景イコライザー制御 */

const BAR_WIDTH = 17; // バーのそれぞれの幅（CSSと一致させる）
const BAR_GAP = 10;    // バー同士の間隔（CSSと一致させる）
const MAX_HEIGHT = 400; // 高さの最大値
const MIN_HEIGHT = 30; // 高さの最小値

const eqContainer = document.getElementById("equalizer-bg");

function createBars() {
    eqContainer.innerHTML = "";

    const screenWidth = window.innerWidth;
    const barCount = Math.floor(screenWidth / (BAR_WIDTH + BAR_GAP));

    for (let i = 0; i < barCount; i++) {
        const bar = document.createElement("div");
        bar.className = "eq-bar";
        bar.style.height = `${Math.random() * MAX_HEIGHT}px`;
        eqContainer.appendChild(bar);
    }
}

// 初期生成
createBars();

// ウィンドウサイズ変更時に再生成
window.addEventListener("resize", createBars);

// 高さをランダム更新
setInterval(() => {
    document.querySelectorAll(".eq-bar").forEach(bar => {
        const randomHeight =
            Math.random() * (MAX_HEIGHT - MIN_HEIGHT) + MIN_HEIGHT;
        bar.style.height = `${randomHeight}px`;
    });
}, 240); // 数値を小さくすると激しく動く

// 横スクロールをホイールで実現するためのJavaScript
document.querySelector('.hero').addEventListener('wheel', function(e) {
  if (e.deltaY !== 0) {
    this.scrollLeft += e.deltaY; // 横方向にスクロール
    e.preventDefault(); // デフォルトの縦スクロールを無効にする
  }
});
