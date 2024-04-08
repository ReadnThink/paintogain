<script setup lang="ts">
import axios from 'axios'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const feeds = ref([])
axios.get('/my-backend-api/feeds?page=1&size=5')
  .then(response => {
    console.log(response)
    response.data.forEach((r: any) => {
      feeds.value.push(r)
    })
  })

const moveToRead = () => {
  router.push({ name: 'read' })
}
</script>

<template>
  <ul>
    <li v-for="feed in feeds" :key="feeds.id" @click="moveToRead()">
      <div>
        <router-link :to="{ name: 'read', params: {feedId : feed.id} }" >{{ feed.title }} </router-link>
      </div>

      <div>
        {{ feed.content }}
      </div>

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