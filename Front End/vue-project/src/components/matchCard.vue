<script setup>
import { computed } from "vue";

const props = defineProps({
  match: { type: Object, required: true },
  defaultOpen: { type: Boolean, default: false },
});

const resultLabel = computed(() => (props.match.win ? "WIN" : "LOSE"));
const resultClass = computed(() => (props.match.win ? "win" : "lose"));

const kdaText = computed(() => `${props.match.kills}/${props.match.deaths}/${props.match.assists}`);

function safeArray(v) {
  return Array.isArray(v) ? v : [];
}

function formatNumber(n) {
  if (n === null || n === undefined || Number.isNaN(Number(n))) return "—";
  return Intl.NumberFormat().format(Number(n));
}

function formatDateTime(iso) {
  if (!iso) return "—";
  const d = new Date(iso);
  if (Number.isNaN(d.getTime())) return String(iso);
  return new Intl.DateTimeFormat(undefined, {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  }).format(d);
}

function kdaFor(p) {
  const k = p?.kills;
  const d = p?.deaths;
  const a = p?.assists;
  if (k == null || d == null || a == null) return "—";
  return `${k}/${d}/${a}`;
}

const teamMates = computed(() => safeArray(props.match.teamMates));
const enemyTeam = computed(() => safeArray(props.match.enemyTeam));
</script>

<template>
  <details class="match-card" :open="defaultOpen">
    <summary class="summary">
      <span class="pill" :class="resultClass">{{ resultLabel }}</span>
      <span class="champ">{{ match.championName }}</span>
      <span class="kda">{{ kdaText }}</span>
    </summary>

    <div class="body">
      <div class="top">
        <div class="title">
          <strong>{{ match.matchId }}</strong>
          <div class="sub">{{ formatDateTime(match.gameStartTime) }}</div>
        </div>

        <div class="stats">
          <div class="stat">
            <span class="label">Damage Dealt</span>
            <span class="value">{{ formatNumber(match.damageDealt) }}</span>
          </div>
          <div class="stat">
            <span class="label">Damage Taken</span>
            <span class="value">{{ formatNumber(match.damageTaken) }}</span>
          </div>
        </div>
      </div>

      <div class="rosters">
        <section class="panel">
          <h4>Your Team</h4>

          <div class="thead">
            <span>Player</span>
            <span class="thead-right">
              <span class="col kda-col">K/D/A</span>
              <span class="col dmg-col">Dealt</span>
              <span class="col dmg-col">Taken</span>
            </span>
          </div>

          <ul class="list" v-if="teamMates.length">
            <li v-for="(p, idx) in teamMates" :key="p.gameName + '#' + p.tagLine + idx" class="row">
              <span class="left">
                <span class="champ-mini">{{ p.championName ?? "—" }}</span>
                <span class="name">{{ p.gameName }}#{{ p.tagLine }}</span>
              </span>

              <span class="right">
                <span class="kda-mini kda-col">{{ kdaFor(p) }}</span>
                <span class="num dmg-col">{{ formatNumber(p.damageDealt) }}</span>
                <span class="num dmg-col">{{ formatNumber(p.damageTaken) }}</span>

                <span class="rank" v-if="p.rank">{{ p.rank }} {{ p.tier }}</span>
                <span class="lp" v-if="p.leaguePoints != null">{{ p.leaguePoints }} LP</span>
              </span>
            </li>
          </ul>
          <div v-else class="empty">No team data.</div>
        </section>

        <section class="panel">
          <h4>Enemy Team</h4>

          <div class="thead">
            <span>Player</span>
            <span class="thead-right">
              <span class="col kda-col">K/D/A</span>
              <span class="col dmg-col">Dealt</span>
              <span class="col dmg-col">Taken</span>
            </span>
          </div>

          <ul class="list" v-if="enemyTeam.length">
            <li v-for="(p, idx) in enemyTeam" :key="p.gameName + '#' + p.tagLine + idx" class="row">
              <span class="left">
                <span class="champ-mini">{{ p.championName ?? "—" }}</span>
                <span class="name">{{ p.gameName }}#{{ p.tagLine }}</span>
              </span>

              <span class="right">
                <span class="kda-mini kda-col">{{ kdaFor(p) }}</span>
                <span class="num dmg-col">{{ formatNumber(p.damageDealt) }}</span>
                <span class="num dmg-col">{{ formatNumber(p.damageTaken) }}</span>

                <span class="rank" v-if="p.rank">{{ p.rank }} {{ p.tier }}</span>
                <span class="lp" v-if="p.leaguePoints != null">{{ p.leaguePoints }} LP</span>
              </span>
            </li>
          </ul>
          <div v-else class="empty">No enemy data.</div>
        </section>
      </div>
    </div>
  </details>
</template>

<style scoped>
.match-card {
  --mc-bg: #ffffff;
  --mc-surface-2: #fafafa;
  --mc-text: #111827;
  --mc-text-2: #374151;
  --mc-muted: #6b7280;
  --mc-border: #e5e7eb;
  --mc-divider: rgba(229, 231, 235, 0.9);

  --mc-win-bg: #ecfdf5;
  --mc-win-text: #065f46;
  --mc-win-border: #a7f3d0;

  --mc-lose-bg: #fef2f2;
  --mc-lose-text: #991b1b;
  --mc-lose-border: #fecaca;

  --mc-chip-bg: #eef2ff;
  --mc-chip-text: #3730a3;
  --mc-chip-border: #c7d2fe;

  --mc-shadow: 0 10px 30px rgba(17, 24, 39, 0.08);

  border: 1px solid var(--mc-border);
  border-radius: 12px;
  background: var(--mc-bg);
  padding: 10px 12px;
  margin: 10px 0;
  box-shadow: var(--mc-shadow);
}

/* Auto dark mode */
@media (prefers-color-scheme: dark) {
  .match-card {
    --mc-bg: #111827;
    --mc-surface-2: #0f172a;
    --mc-text: #e5e7eb;
    --mc-text-2: #cbd5e1;
    --mc-muted: #94a3b8;
    --mc-border: rgba(148, 163, 184, 0.18);
    --mc-divider: rgba(148, 163, 184, 0.18);

    --mc-win-bg: rgba(16, 185, 129, 0.14);
    --mc-win-text: #6ee7b7;
    --mc-win-border: rgba(110, 231, 183, 0.28);

    --mc-lose-bg: rgba(239, 68, 68, 0.14);
    --mc-lose-text: #fca5a5;
    --mc-lose-border: rgba(252, 165, 165, 0.26);

    --mc-chip-bg: rgba(99, 102, 241, 0.14);
    --mc-chip-text: #c7d2fe;
    --mc-chip-border: rgba(199, 210, 254, 0.22);

    --mc-shadow: 0 16px 40px rgba(0, 0, 0, 0.45);
  }
}

/* Optional manual override: put class="dark" on <html> */
:global(html.dark) .match-card {
  --mc-bg: #111827;
  --mc-surface-2: #0f172a;
  --mc-text: #e5e7eb;
  --mc-text-2: #cbd5e1;
  --mc-muted: #94a3b8;
  --mc-border: rgba(148, 163, 184, 0.18);
  --mc-divider: rgba(148, 163, 184, 0.18);

  --mc-win-bg: rgba(16, 185, 129, 0.14);
  --mc-win-text: #6ee7b7;
  --mc-win-border: rgba(110, 231, 183, 0.28);

  --mc-lose-bg: rgba(239, 68, 68, 0.14);
  --mc-lose-text: #fca5a5;
  --mc-lose-border: rgba(252, 165, 165, 0.26);

  --mc-chip-bg: rgba(99, 102, 241, 0.14);
  --mc-chip-text: #c7d2fe;
  --mc-chip-border: rgba(199, 210, 254, 0.22);

  --mc-shadow: 0 16px 40px rgba(0, 0, 0, 0.45);
}

.summary {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 12px;
  list-style: none;
  color: var(--mc-text);
}
.summary::-webkit-details-marker {
  display: none;
}

.pill {
  font-weight: 800;
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 999px;
  border: 1px solid transparent;
}

.pill.win {
  background: var(--mc-win-bg);
  color: var(--mc-win-text);
  border-color: var(--mc-win-border);
}

.pill.lose {
  background: var(--mc-lose-bg);
  color: var(--mc-lose-text);
  border-color: var(--mc-lose-border);
}

.champ {
  font-weight: 800;
  flex: 1;
  color: var(--mc-text);
}

.kda {
  font-family: "Segoe UI";
  opacity: 0.9;
  color: var(--mc-text-2);
}

.body {
  margin-top: 10px;
  color: var(--mc-text);
}

.top {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  flex-wrap: wrap;
  margin-bottom: 12px;
}

.title .sub {
  color: var(--mc-muted);
  font-size: 12px;
  margin-top: 2px;
}

.stats {
  display: flex;
  gap: 14px;
  flex-wrap: wrap;
}

.stat {
  background: var(--mc-surface-2);
  border: 1px solid var(--mc-border);
  border-radius: 10px;
  padding: 8px 10px;
  min-width: 150px;
}

.label {
  display: block;
  font-size: 12px;
  color: var(--mc-muted);
}

.value {
  display: block;
  font-weight: 800;
  color: var(--mc-text);
}

.rosters {
  display: grid;
  grid-template-columns: repeat(2, minmax(260px, 1fr));
  gap: 10px;
}

.panel {
  border: 1px solid var(--mc-border);
  border-radius: 10px;
  padding: 10px;
  background: var(--mc-surface-2);
}

.panel h4 {
  margin: 0 0 8px 0;
  font-size: 13px;
  color: var(--mc-text);
}

.list {
  margin: 0;
  padding: 0;
  list-style: none;
}

.row {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  padding: 6px 0;
  border-bottom: 1px dashed var(--mc-divider);
}

.row:last-child {
  border-bottom: none;
}

.left {
  display: flex;
  gap: 10px;
  align-items: center;
  min-width: 0;
}

.champ-mini {
  font-weight: 800;
  font-size: 12px;
  padding: 3px 8px;
  border-radius: 999px;
  background: var(--mc-chip-bg);
  border: 1px solid var(--mc-chip-border);
  color: var(--mc-chip-text);
  white-space: nowrap;
}

.name {
  font-weight: 600;
  color: var(--mc-text);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 220px;
}

.right {
  display: flex;
  gap: 8px;
  align-items: center;
  justify-content: flex-end;
  flex-wrap: wrap;
  color: var(--mc-text-2);
}

.kda-mini {
  font-family: "Segoe UI";
  font-size: 12px;
  color: var(--mc-text-2);
}

.rank,
.lp {
  font-size: 12px;
  color: var(--mc-muted);
  white-space: nowrap;
}

.thead {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  padding: 6px 0 8px 0;
  margin-bottom: 6px;
  border-bottom: 1px solid var(--mc-border);
  color: var(--mc-muted);
  font-size: 12px;
  font-weight: 700;
}

.thead-right {
  display: flex;
  gap: 10px;
  align-items: center;
  justify-content: flex-end;
  flex-wrap: nowrap;
}

.col {
  display: inline-block;
  text-align: right;
}

.kda-col {
  width: 86px;
}

.dmg-col {
  width: 78px;
}

.num {
  font-family: "Segoe UI";
  font-size: 12px;
  color: var(--mc-text-2);
}

.empty {
  color: var(--mc-muted);
  font-size: 12px;
}
</style>