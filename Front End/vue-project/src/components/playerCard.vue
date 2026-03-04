<script setup>
import MatchCard from "@/components/matchCard.vue";

const props = defineProps({
  playerData: { type: Object, required: true }
});
</script>

<template>
  <section>
    <h2>{{ playerData.gameName }}#{{ playerData.tagLine }}'s Data</h2>

    <div v-if="Array.isArray(playerData.matches) && playerData.matches.length">
      <h3>Recent Matches</h3>

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