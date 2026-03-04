<script setup>
import Header from "@/components/header.vue";
import PlayerCard from "@/components/playerCard.vue";
import {reactive, ref} from "vue";

const query = ref("");
const fetchSuccess = ref(false);
const playerData = ref(null);

async function search() {
  if (!query.value) return;
  if (!/^[a-zA-Z0-9]+#[a-zA-Z0-9]+$/.test(query.value)) return alert(
      "Please enter a valid gameName#tagLine"
  )
  let gameName = query.value.split("#")[0];
  let tagLine = query.value.split("#")[1];
  const url = `http://localhost:8080/api/players/${gameName}/${tagLine}`;
  try {
    const response = await fetch(url, {signal: AbortSignal.timeout(7000)});
    if (!response.ok) {
      throw new Error(`Response status: ${response.status}`);
    }
    const result = await response.json();
    playerData.value = result;
    fetchSuccess.value = true;
  } catch (error) {
    fetchSuccess.value = false;
    console.error(error.message);
  }
}
</script>

<template>
  <Header/>
  <main>
    <input
        v-model="query"
        type="search"
        placeholder="gameName#tagLine"
        @keyup.enter="search"
    />
    <button type="button" @click="search">Search</button>
    <div v-if="fetchSuccess">
      <PlayerCard :playerData="playerData"/>
    </div>
  </main>
</template>

<style scoped>

</style>