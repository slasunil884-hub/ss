:root {
  --bg: #020408;
  --bg-soft: #0b1120;
  --panel: rgba(15, 23, 42, 0.82);
  --panel-strong: rgba(9, 14, 26, 0.96);
  --line: rgba(0, 240, 255, 0.24);
  --primary: #ff6b00;
  --secondary: #00f0ff;
  --gold: #ffd700;
  --green: #00e676;
  --text: #e5eefb;
  --muted: #99a9c4;
  --danger: #ff4d6d;
  --shadow: rgba(0, 240, 255, 0.26);
}

* {
  box-sizing: border-box;
}

html {
  scroll-behavior: smooth;
}

body {
  margin: 0;
  min-height: 100vh;
  font-family: "Inter", sans-serif;
  background:
    radial-gradient(circle at top, rgba(0, 240, 255, 0.12) 0%, transparent 30%),
    linear-gradient(180deg, #020408 0%, #060b14 100%);
  color: var(--text);
}

button,
input {
  font: inherit;
}

button {
  cursor: pointer;
}

.bg-grid,
.noise {
  position: fixed;
  inset: 0;
  pointer-events: none;
}

.bg-grid {
  background-image:
    linear-gradient(rgba(255,255,255,0.02) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255,255,255,0.02) 1px, transparent 1px);
  background-size: 30px 30px;
  mask-image: radial-gradient(circle at center, black 35%, transparent 100%);
}

.noise {
  background-image: radial-gradient(rgba(255,255,255,0.05) 0.75px, transparent 0.75px);
  background-size: 14px 14px;
  opacity: 0.2;
}

.wrap {
  width: min(1180px, calc(100% - 32px));
  margin: 0 auto;
}

.topbar {
  position: sticky;
  top: 0;
  z-index: 30;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 18px;
  padding: 18px 0 12px;
  backdrop-filter: blur(12px);
  background: rgba(2, 4, 8, 0.7);
  border-bottom: 1px solid rgba(255,255,255,0.04);
}

.brand-wrap {
  display: flex;
  align-items: center;
  gap: 12px;
}

.brand-mark {
  display: grid;
  place-items: center;
  width: 42px;
  height: 42px;
  border-radius: 12px;
  background: linear-gradient(135deg, var(--primary), var(--secondary));
  color: #020408;
  font-family: "Orbitron", sans-serif;
  font-weight: 900;
  box-shadow: 0 0 25px rgba(255, 107, 0, 0.5);
}

.brand-name {
  font-family: "Orbitron", sans-serif;
  font-weight: 800;
  letter-spacing: 0.12em;
  font-size: 0.9rem;
}

.brand-sub {
  font-size: 0.68rem;
  color: var(--muted);
  letter-spacing: 0.1em;
}

.main-nav {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
  justify-content: center;
}

.nav-pill,
.lang-chip,
.mini-btn,
.prompt-btn,
.module-card,
.primary-btn,
.secondary-btn {
  border: 1px solid rgba(255,255,255,0.08);
  background: rgba(6, 15, 25, 0.8);
  color: var(--text);
  border-radius: 12px;
  transition: 180ms ease;
}

.nav-pill {
  padding: 9px 12px;
  font-size: 0.72rem;
  font-weight: 700;
  letter-spacing: 0.12em;
  color: var(--muted);
}

.nav-pill.active,
.nav-pill:hover {
  color: white;
  border-color: rgba(0, 240, 255, 0.45);
  box-shadow: inset 0 0 0 1px rgba(0, 240, 255, 0.15), 0 0 18px rgba(0, 240, 255, 0.12);
}

.topbar-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.lang-chip,
.mini-btn {
  padding: 8px 10px;
  font-size: 0.7rem;
  font-weight: 700;
  letter-spacing: 0.08em;
}

.status-indicator {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  border-radius: 999px;
  border: 1px solid rgba(0, 240, 255, 0.25);
  background: rgba(2, 25, 30, 0.7);
  color: var(--secondary);
  font-size: 0.7rem;
  letter-spacing: 0.1em;
  font-weight: 700;
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--secondary);
  box-shadow: 0 0 12px rgba(0, 240, 255, 0.9);
  animation: pulse 1.5s infinite ease-in-out;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); opacity: 1; }
  50% { transform: scale(1.3); opacity: 0.7; }
}

.page-shell {
  padding: 26px 0 48px;
}

.hero {
  display: grid;
  grid-template-columns: 1.1fr 0.9fr;
  gap: 30px;
  align-items: center;
  padding: 34px 0 18px;
}

.eyebrow {
  margin: 0 0 12px;
  color: var(--secondary);
  letter-spacing: 0.18em;
  font-weight: 700;
  font-size: 0.7rem;
  text-transform: uppercase;
}

h1 {
  margin: 0;
  font-family: "Orbitron", sans-serif;
  font-size: clamp(2.05rem, 4vw, 4.6rem);
  line-height: 1.05;
  letter-spacing: -0.04em;
}

.gradient-text {
  background: linear-gradient(90deg, var(--primary), var(--secondary), var(--gold));
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.lede {
  margin: 18px 0 0;
  max-width: 620px;
  color: var(--muted);
  font-size: 1.02rem;
  line-height: 1.7;
}

.cta-row {
  display: flex;
  gap: 14px;
  margin-top: 24px;
  flex-wrap: wrap;
}

.primary-btn,
.secondary-btn {
  padding: 14px 20px;
  border-radius: 14px;
  border: none;
  font-weight: 800;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.primary-btn {
  background: linear-gradient(135deg, var(--primary), #ff9a3d);
  color: #0b0d12;
  box-shadow: 0 12px 30px rgba(255, 107, 0, 0.28);
}

.secondary-btn {
  background: rgba(7, 14, 22, 0.8);
  color: var(--text);
  border: 1px solid rgba(0, 240, 255, 0.28);
}

.primary-btn.compact {
  padding: 12px 18px;
  font-size: 0.72rem;
}

.stats-row {
  display: flex;
  gap: 14px;
  margin-top: 26px;
  flex-wrap: wrap;
}

.stat-box {
  min-width: 110px;
  padding: 14px 16px;
  border-radius: 14px;
  background: rgba(12, 18, 29, 0.9);
  border: 1px solid rgba(255,255,255,0.06);
  box-shadow: inset 0 0 0 1px rgba(0, 240, 255, 0.04);
}

.stat-label {
  display: block;
  color: var(--muted);
  font-size: 0.68rem;
  letter-spacing: 0.08em;
  margin-bottom: 6px;
  text-transform: uppercase;
}

.stat-box strong {
  font-size: 1.15rem;
}

.hero-visual {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 18px;
  min-height: 420px;
}

.orb-shell {
  position: relative;
  width: min(360px, 78vw);
  aspect-ratio: 1;
  display: grid;
  place-items: center;
}

.orb-ring {
  position: absolute;
  inset: 0;
  border-radius: 50%;
  border: 1px dashed rgba(0, 240, 255, 0.38);
  animation: spin 16s linear infinite;
}

.ring-b {
  inset: 18%;
  border-color: rgba(255, 107, 0, 0.42);
  animation-direction: reverse;
}

.orb-core {
  position: relative;
  width: 62%;
  aspect-ratio: 1;
  border-radius: 50%;
  background: radial-gradient(circle at 30% 30%, #ffb84d, #ff6b00 30%, #0b1120 68%, #030708 100%);
  box-shadow:
    0 0 30px rgba(255, 107, 0, 0.3),
    0 0 90px rgba(0, 240, 255, 0.2),
    inset 0 0 28px rgba(255,255,255,0.2);
  display: grid;
  place-items: center;
  animation: pulse-core 3s infinite ease-in-out;
}

.core-center {
  width: 60%;
  height: 60%;
  border-radius: 50%;
  display: grid;
  place-items: center;
  background: rgba(4, 9, 14, 0.82);
  border: 1px solid rgba(0, 240, 255, 0.3);
  font-family: "Orbitron", sans-serif;
  font-size: clamp(2rem, 3vw, 3.1rem);
  font-weight: 900;
  color: var(--secondary);
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@keyframes pulse-core {
  0%, 100% { transform: scale(1); box-shadow: 0 0 28px rgba(255, 107, 0, 0.35), 0 0 60px rgba(0, 240, 255, 0.2); }
  50% { transform: scale(1.04); box-shadow: 0 0 32px rgba(0, 240, 255, 0.45), 0 0 90px rgba(255, 107, 0, 0.28); }
}

.telemetry-box {
  width: min(280px, 80%);
  border-radius: 16px;
  background: rgba(8, 14, 24, 0.9);
  border: 1px solid rgba(0, 240, 255, 0.14);
  padding: 16px 18px;
  box-shadow: 0 20px 30px rgba(0,0,0,0.2);
}

.telemetry-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  font-size: 0.74rem;
  text-transform: uppercase;
  letter-spacing: 0.12em;
  border-bottom: 1px solid rgba(255,255,255,0.03);
}

.telemetry-row:last-child {
  border-bottom: none;
}

.panel-grid {
  display: grid;
  grid-template-columns: 1.4fr 0.8fr;
  gap: 20px;
  margin-top: 18px;
}

.glass {
  background: rgba(12, 18, 30, 0.82);
  border: 1px solid rgba(0, 240, 255, 0.12);
  border-radius: 22px;
  box-shadow: 0 22px 40px rgba(0,0,0,0.18);
}

.panel {
  padding: 20px;
}

.panel-header,
.feature-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  font-family: "Orbitron", sans-serif;
  font-size: 0.72rem;
  color: var(--secondary);
  font-weight: 700;
  letter-spacing: 0.12em;
}

.tiny-tag {
  font-size: 0.62rem;
  padding: 6px 8px;
  border-radius: 999px;
  color: #fff;
  background: rgba(255, 107, 0, 0.15);
  border: 1px solid rgba(255, 107, 0, 0.25);
}

.module-grid {
  margin-top: 20px;
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
}

.module-card {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  gap: 10px;
  min-height: 116px;
  text-align: center;
  font-weight: 700;
  color: var(--muted);
}

.module-card.active,
.module-card:hover {
  background: rgba(0, 240, 255, 0.06);
  border-color: rgba(0, 240, 255, 0.26);
  color: var(--text);
  box-shadow: inset 0 0 0 1px rgba(0,240,255,0.12), 0 0 18px rgba(0,240,255,0.12);
}

.module-emoji {
  font-size: 1.6rem;
}

.diag-list {
  margin-top: 18px;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.diag-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: var(--muted);
  padding-bottom: 10px;
  border-bottom: 1px solid rgba(255,255,255,0.04);
}

.good {
  color: var(--green);
  font-weight: 700;
}

.workspace {
  display: grid;
  grid-template-columns: 1.3fr 0.7fr;
  gap: 20px;
  margin-top: 24px;
}

.console-panel,
.side-panel {
  padding: 18px;
}

.console-topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  margin-bottom: 18px;
}

.console-title {
  font-family: "Orbitron", sans-serif;
  font-weight: 800;
  font-size: 0.74rem;
  letter-spacing: 0.12em;
  color: var(--secondary);
}

.console-tools {
  display: flex;
  align-items: center;
  gap: 8px;
}

.chat-window {
  min-height: 260px;
  max-height: 360px;
  overflow: auto;
  padding: 16px;
  border-radius: 16px;
  background: rgba(2, 6, 12, 0.9);
  border: 1px solid rgba(255,255,255,0.06);
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.chat-msg {
  display: flex;
  gap: 8px;
  align-items: flex-start;
  line-height: 1.6;
  font-size: 0.92rem;
}

.chat-msg.system {
  color: #dfeeff;
}

.chat-msg.user {
  justify-content: flex-end;
  color: #fff;
}

.chat-msg.user .role {
  color: var(--gold);
}

.chat-msg .role {
  color: var(--secondary);
  font-weight: 800;
  white-space: nowrap;
}

.prompt-row {
  margin-top: 16px;
  display: flex;
  gap: 12px;
}

.prompt-row input {
  flex: 1;
  border-radius: 14px;
  border: 1px solid rgba(0, 240, 255, 0.16);
  background: rgba(5, 10, 18, 0.9);
  color: var(--text);
  padding: 14px 16px;
  outline: none;
}

.quick-prompts {
  margin-top: 18px;
  display: grid;
  gap: 12px;
}

.prompt-btn {
  text-align: left;
  padding: 12px 14px;
  color: var(--muted);
}

.prompt-btn:hover {
  color: var(--text);
  border-color: rgba(255, 107, 0, 0.25);
}

.feature-row {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 20px;
  margin-top: 26px;
}

.feature-card {
  padding: 20px 18px;
}

.feature-card h3 {
  margin: 18px 0 12px;
  font-family: "Orbitron", sans-serif;
  font-size: 1.15rem;
}

.feature-card ul {
  margin: 0;
  padding-left: 18px;
  color: var(--muted);
  line-height: 1.8;
}

@media (max-width: 900px) {
  .topbar {
    flex-wrap: wrap;
    justify-content: center;
  }

  .hero,
  .panel-grid,
  .workspace,
  .feature-row {
    grid-template-columns: 1fr;
  }

  .main-nav {
    width: 100%;
  }
}

@media (max-width: 560px) {
  .brand-name {
    letter-spacing: 0.06em;
    font-size: 0.75rem;
  }

  .brand-sub {
    letter-spacing: 0.05em;
  }

  .module-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .topbar {
    padding-top: 12px;
  }

  .prompt-row {
    flex-direction: column;
  }
}
