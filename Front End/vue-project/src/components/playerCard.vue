<script setup>
import MatchCard from "@/components/matchCard.vue";

const props = defineProps({
  playerData: { type: Object, required: true }
});
</script>

<template>
  <section>
    <h2>{{ playerData.gameName }}'s Recent Matches (Winrate: {{playerData.winRateLast20}}%  )</h2>

    <h2>Rank: {{playerData.rank}} {{playerData.tier}} {{playerData.leaguePoints}} LP</h2>

    <div v-if="Array.isArray(playerData.matches) && playerData.matches.length">
      <MatchCard
          v-for="m in playerData.matches"
          :key="m.matchId"
          :match="m"
      />
    </div>

    <div v-else>
      No matches found.
    </div>
  </section>
</template>

<style scoped>
</style>