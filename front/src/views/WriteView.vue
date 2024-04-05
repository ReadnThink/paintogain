<script setup lang="ts">
import {ref} from "vue";
import axios from "axios";

const title = ref("")
const content = ref("")

const write = function () {
  axios.post('http://localhost:8080/feeds', {
    title: title.value,
    content: content.value
  })
}
</script>

<template>
  <div>
    <el-input v-model="title" placeholder="제목을 입력해주세요"/>
  </div>

  <div class="mt-2">
    <el-input v-model="content" type="textarea" rows="15"/>
  </div>

  <el-button type="primary" @click="write()">피드 작성</el-button>
</template>

<style>

</style>