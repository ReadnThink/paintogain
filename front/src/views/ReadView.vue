<script setup lang="ts">
import { onMounted, ref } from 'vue'
import axios from 'axios'

const props = defineProps({
  feedId: {
    type: [Number, String],
    required: true
  }
})

const feed = ref({
  id: 0,
  title: "",
  content: "",
})

onMounted(() => {
  axios.get(`/my-backend-api/feeds/${props.feedId}`).then(response => {
      feed.value = response.data;
    });
});
</script>

<template>
  <h2>{{ feed.title }}</h2>
  <div>{{ feed.content }}</div>
</template>