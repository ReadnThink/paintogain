<script setup lang="ts">
import axios from 'axios'
import { ref } from 'vue'

const feeds = ref([]);
axios.get('/my-backend-api/feeds?page=1&size=5')
.then(response => {
  console.log(response)
  response.data.forEach((r: any) => {
    feeds.value.push(r);
  })
})
</script>

<template>
  <ul>
    <li v-for="feed in feeds" :key="feeds.id">
      <div>{{ feed.title }}</div>
      <div>{{ feed.content }}</div>

    </li>
  </ul>
</template>

<style scoped>
li {
  margin-bottom: 1rem;
}

li:last-child {
  margin-bottom: 0;
}

</style>