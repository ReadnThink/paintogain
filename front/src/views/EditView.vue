<script setup lang="ts">
import {ref} from "vue";
import axios from "axios";
import { useRouter } from 'vue-router';

const router = useRouter();

const feed = ref({
  id: 0,
  title: "",
  content: "",
});

const props = defineProps({
  feedId: {
    type: [Number, String],
    required: true
  },
});

axios.get(`/my-backend-api/feeds/${props.feedId}`).then(response => {
  feed.value = response.data;
});

const edit = () => {
  axios.patch(`/my-backend-api/feeds/${props.feedId}`, feed.value).then(() => {
    router.replace({name: "home"});
  });
};

</script>

<template>
  <div>
    <el-input v-model="feed.title" placeholder="제목을 입력해주세요"/>
  </div>

  <div class="mt-2">
    <el-input v-model="feed.content" type="textarea" rows="15"/>
  </div>

  <el-button type="warning" @click="edit()">수정완료</el-button>
</template>

<style>

</style>