<script setup lang="ts">
import { onMounted, ref } from 'vue'
import axios from 'axios'
import router from '@/router'

const props = defineProps({
  feedId: {
    type: [Number, String],
    required: true
  },
});

const feed = ref({
  id: 0,
  title: "",
  content: "",
})

const moveToEdit = () => {
  router.push({name: "edit", params: {feedId: props.feedId}})
}

onMounted(() => {
  axios.get(`/my-backend-api/feeds/${props.feedId}`).then(response => {
      feed.value = response.data;
    });
});
</script>

<template>
  <h2>{{ feed.title }}</h2>
  <div>{{ feed.content }}</div>

  <el-button type="warning" @click="moveToEdit()">수정</el-button>
</template>