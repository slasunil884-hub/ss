<!DOCTYPE html>
<html lang="si">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Shadow Supporter | RAVI Core</title>
    <meta
      name="description"
      content="Shadow Supporter යනු අධ්‍යයනය, සංගීතය, රොබෝටික్స్ සහ AI සහාය සඳහා සකස් කළ අනාගතමය ද්වීමාන කමාන්ඩ් මධ්‍යස්ථානයකි."
    />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
      href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800;900&family=Orbitron:wght@400;500;600;700;800;900&display=swap"
      rel="stylesheet"
    />
    <link rel="stylesheet" href="styles.css" />
  </head>
  <body>
    <div class="bg-grid"></div>
    <div class="noise"></div>

    <header class="topbar wrap">
      <div class="brand-wrap">
        <div class="brand-mark">SS</div>
        <div>
          <div class="brand-name">SHADOW SUPPORTER</div>
          <div class="brand-sub">RAVI CORE // ගෝලීය බුද්ධි මණ්ඩලය</div>
        </div>
      </div>

      <nav class="main-nav" aria-label="Main navigation">
        <button class="nav-pill active" data-target="home">මුල්</button>
        <button class="nav-pill" data-target="chat">කතාබහ</button>
        <button class="nav-pill" data-target="music">සංගීතය</button>
        <button class="nav-pill" data-target="study">අධ්‍යයනය</button>
        <button class="nav-pill" data-target="robotics">රොබෝටික்ஸ்</button>
      </nav>

      <div class="topbar-actions">
        <button id="langToggle" class="lang-chip" aria-label="Toggle language">SI</button>
        <div class="status-indicator">
          <span class="dot"></span>
          සක්‍රීයයි
        </div>
      </div>
    </header>

    <main class="wrap page-shell">
      <section class="hero" id="home">
        <div class="hero-copy">
          <p class="eyebrow">ඒකාබද්ධ අණ මධ්‍යස්ථානය</p>
          <h1>
            ඔබගේ පුද්ගල AI සහකරු
            <span class="gradient-text">අධ්‍යයනය, සංගීතය, රොබෝටික్స్ සහ වීර මෝඩය සඳහා.</span>
          </h1>
          <p class="lede">
            RAVI ඔබේ දෛනික ක්‍රියාකාරකම් අනාගත කාලීන සයිබර් අණ මධ්‍යස්ථානයක් බවට පත් කරයි.
            සිංහල, ඉංග්‍රීසි සහ සිංහලින් English ගැලපෙන වචන සමඟ, ඔබ වෙනුවෙන් සකස් කර ඇත.
          </p>

          <div class="cta-row">
            <button class="primary-btn">RAVI ඇරඹන්න</button>
            <button class="secondary-btn">පද්ධති බලන්න</button>
          </div>

          <div class="stats-row">
            <div class="stat-box">
              <span class="stat-label">පද්ධතිය</span>
              <strong>94.8%</strong>
            </div>
            <div class="stat-box">
              <span class="stat-label">කාලය</span>
              <strong>24/7</strong>
            </div>
            <div class="stat-box">
              <span class="stat-label">වර්ග</span>
              <strong>5 කුටුම්</strong>
            </div>
          </div>
        </div>

        <div class="hero-visual">
          <div class="orb-shell">
            <div class="orb-ring ring-a"></div>
            <div class="orb-ring ring-b"></div>
            <div class="orb-core" aria-label="RAVI holographic core">
              <div class="core-center">
                <span>SS</span>
              </div>
            </div>
          </div>
          <div class="telemetry-box">
            <div class="telemetry-row">
              <span>CPU</span>
              <strong>31%</strong>
            </div>
            <div class="telemetry-row">
              <span>MEM</span>
              <strong>72%</strong>
            </div>
            <div class="telemetry-row">
              <span>NET</span>
              <strong>14 ms</strong>
            </div>
          </div>
        </div>
      </section>

      <section class="panel-grid">
        <article class="glass panel panel-large">
          <div class="panel-header">
            <span>ප්‍රධාන මධ්‍යස්ථානය</span>
            <span class="tiny-tag">සජීවී</span>
          </div>
          <div class="module-grid">
            <button class="module-card active">
              <span class="module-emoji">⚡</span>
              <span>ඉක්මනින් දියත් කරන්න</span>
            </button>
            <button class="module-card">
              <span class="module-emoji">🧠</span>
              <span>AI කර්තෘ</span>
            </button>
            <button class="module-card">
              <span class="module-emoji">🎧</span>
              <span>ශ්‍රව්‍ය</span>
            </button>
            <button class="module-card">
              <span class="module-emoji">📚</span>
              <span>අධ්‍යාපන කුටුව</span>
            </button>
          </div>
        </article>

        <article class="glass panel panel-small">
          <div class="panel-header">
            <span>පද්ධති ගවේෂණය</span>
          </div>
          <div class="diag-list">
            <div class="diag-row">
              <span>බැටරිය</span>
              <span class="good">උච්චිත</span>
            </div>
            <div class="diag-row">
              <span>සංජායනය</span>
              <span class="good">ස්ථාවර</span>
            </div>
            <div class="diag-row">
              <span>ශ්‍රව්‍ය</span>
              <span class="good">සූදානම්</span>
            </div>
          </div>
        </article>
      </section>

      <section class="workspace">
        <div class="glass console-panel">
          <div class="console-topbar">
            <div class="console-title">නියු‍රල් කතාබහ ටර්මිනල්</div>
            <div class="console-tools">
              <button class="mini-btn">SI</button>
              <button class="mini-btn">ආරම්භක</button>
            </div>
          </div>

          <div class="chat-window" id="chatWindow" aria-live="polite">
            <div class="chat-msg system">
              <span class="role">[RAVI]</span>
              <span>Shadow Supporter සක්‍රීයයි. අණ සූදානම්. විග්‍රහ කරන්න, කේත ලියන්න, අධ්‍යාපනයට උදව් කරන්න හෝ සංගීතය පවරන්න.</span>
            </div>
          </div>

          <div class="prompt-row">
            <input id="chatInput" type="text" placeholder="අණයක් ටයිප් කරන්න හෝ RAVIට අසන්න..." />
            <button id="sendBtn" class="primary-btn compact">යවන්න</button>
          </div>
        </div>

        <aside class="glass side-panel">
          <div class="panel-header">
            <span>වීර අණ</span>
          </div>
          <div class="quick-prompts">
            <button class="prompt-btn">ප්‍රභාසංස්ලේෂනය පැහැදිලි කරන්න</button>
            <button class="prompt-btn">ආර්ඩුයිනෝ කේතය ජනනය කරන්න</button>
            <button class="prompt-btn">අද අධ්‍යාපන සැලසුම</button>
            <button class="prompt-btn">නැඹුරු සංගීතය වාදනය කරන්න</button>
          </div>
        </aside>
      </section>

      <section class="feature-row">
        <article class="glass feature-card" id="music">
          <div class="feature-head">
            <span>🎵 සංගීත කුටුව</span>
            <span class="tiny-tag">නැඹුරු</span>
          </div>
          <h3>ගෝලීය පෙනෙන ක්‍රීඩා</h3>
          <ul>
            <li>නැඹුරු, මෘදු, උද්දීපන, භක්තිය</li>
            <li>පිළිවෙළ සහ මිතුරු ලැයිස්තු</li>
            <li>ගෝලීය මෝඩ පෙරහන්</li>
          </ul>
        </article>

        <article class="glass feature-card" id="study">
          <div class="feature-head">
            <span>📚 අධ්‍යාපන කුටුව</span>
            <span class="tiny-tag">1–13</span>
          </div>
          <h3>1 සිට 13 දක්වා ශ්‍රේණි</h3>
          <ul>
            <li>ක්‍රියාකාරී සිහිකැඳවීම් ෆ්ලෑෂ්කාඩ්</li>
            <li>සූත්‍ර ගබඩාව සහ ප්‍රශ්න</li>
            <li>දිනපතා සැලසුම සහ Planner</li>
          </ul>
        </article>

        <article class="glass feature-card" id="robotics">
          <div class="feature-head">
            <span>🤖 රොබෝටිකස්</span>
            <span class="tiny-tag">AI</span>
          </div>
          <h3>ආර්ඩුයිනෝ සහ පාලන තර්ක</h3>
          <ul>
            <li>Uno, Nano, Mega, ESP32</li>
            <li>Serial monitor තත්ත්වය</li>
            <li>AI කේත ජනනය</li>
          </ul>
        </article>
      </section>
    </main>

    <script src="script.js"></script>
  </body>
</html>
