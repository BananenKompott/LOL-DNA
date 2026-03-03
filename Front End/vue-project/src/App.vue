<script setup>
import Header from "@/components/header.vue";
import { ref } from "vue";

const query = ref("");

function search() {
  console.log(query.value);
}
</script>

<template>
  <Header />
  <main>
    <input
        v-model="query"
        type="search"
        placeholder="Search..."
        @keyup.enter="search"
    />
    <button type="button" @click="search">Search</button>
  </main>
</template>

<style scoped>

</style>