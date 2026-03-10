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
    <div style="text-align: center">
      <input
          v-model="query"
          type="search"
          placeholder="gameName#tagLine"
          @keyup.enter="search"
          style=""
      />
      &nbsp;&nbsp;&nbsp;
      <button type="button" @click="search">&nbsp;&nbsp;Search&nbsp;&nbsp;</button>
    </div>
    <br>
    <div v-if="fetchSuccess">
      <PlayerCard :playerData="playerData"/>
    </div>
  </main>
</template>

<style scoped>
* {
  color: #ffffff;
}

input {
  color: black;
  align-content: center;
  border-radius: 5px;
  border-color: rgba(0,0,0,0);
  font-size: 1rem;
  padding: 0.5rem;
}

button {
  color: white;
  border-radius: 5px;
  border-color: rgba(0,0,0,0);
  font-size: 1rem;
  padding: 0.8rem;
  background-color: #111827;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #3c4f83;
}
</style>