<script setup lang="ts">
import axios from 'axios'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const feeds = ref([])
axios.get('/my-backend-api/feeds?page=1&size=5')
  .then(response => {
    response.data.forEach((r: any) => {
      feeds.value.push(r);
    })
  })

const moveToRead = () => {
  router.push({ name: 'read' })
}
</script>

<template>
  <ul>
    <li v-for="feed in feeds" :key="feeds.id" @click="moveToRead()">
      <div class="title">
        <router-link :to="{ name: 'read', params: {feedId : feed.id} }">{{ feed.title }}</router-link>
      </div>

      <div class="content">
        {{ feed.content }}
      </div>

      <div class="sub d-flex">
        <div class="category">
           개발::하드코딩
        </div>
        <div class="regDate">
          2024-04-09 11:09:22 ::하드코딩
        </div>
      </div>
    </li>
  </ul>
</template>

<style scoped lang="scss">
ul {
  list-style: none;
  padding: 0;

  li {
    margin-bottom: 1.6rem;

    .title {
      a {
        font-size: 1.1rem;
        color: #383838;
        text-decoration: none;
      }

      &:hover {
        text-decoration: underline;
      }
    }

    .content {
      font-size: 0.85rem;
      margin-top: 8px;
      color: #6b6b6b;
    }
    &:last-child {
      margin-bottom: 0;
    }
    .sub {
      margin-top: 7px;
      font-size: 0.78rem;

      .regDate {
        margin-left: 10px;
        color: #6b6b6b;
      }
    }
  }
}

</style>