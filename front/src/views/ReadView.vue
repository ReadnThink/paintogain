<script setup lang="ts">
import { onMounted, ref } from 'vue'
import axios from 'axios'
import router from '@/router'

const props = defineProps({
  feedId: {
    type: [Number, String],
    required: true
  }
})

const feed = ref({
  id: 0,
  title: '',
  content: ''
})

const moveToEdit = () => {
  router.push({ name: 'edit', params: { feedId: props.feedId } })
}

onMounted(() => {
  axios.get(`/my-backend-api/feeds/${props.feedId}`).then(response => {
    feed.value = response.data
  })
})
</script>

<template>

  <el-row>
    <el-col>
      <h2 class="title">{{ feed.title }}</h2>

      <div class="sub d-flex">
        <div class="category">
          개발::하드코딩
        </div>
        <div class="regDate">
          2024-04-09 11:09:22 ::하드코딩
        </div>
      </div>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="content">{{ feed.content }}</div>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="d-flex justify-content-end">
        <el-button type="warning" @click="moveToEdit()">수정</el-button>
      </div>
    </el-col>
  </el-row>
</template>

<style scoped lang="scss">
.title {
  font-size: 1.6rem;
  font-weight: 600;
  color: #383838;
  margin: 0;
}

.content {
  font-size: 0.85rem;
  margin-top: 12px;
  color: #7e7e7e;
  white-space: break-spaces;

}

.sub {
  margin-top: 10px;
  font-size: 0.78rem;

  .regDate {
    margin-left: 10px;
    color: #6b6b6b;
  }
}
</style>