<script setup lang="ts">

import {reactive} from "vue";
import { useRouter } from "vue-router";
import Login from "@/entity/user/Login";
import axios, {AxiosError, type AxiosResponse} from "axios";
import {ElMenu, ElMessage} from "element-plus";

const router = useRouter()
const state = reactive({
  login: new Login(),
})

function doLogin() {
  // Security EmailPasswordAuthFilter 인증
  axios.post("/my-backend-api/auth/login", state.login)
      .then((response: AxiosResponse) => {
        ElMessage({ type: 'success', message: '환영합니다 :)' });
        router.replace('/');

      })
      .catch((e: AxiosError) => {
        ElMessage({ type: 'error', message: e.response?.data.message });
      });
}
</script>

<template>
  <el-row>
    <el-col :span="10" :offset="7">
      <el-form label-position="top">
        <el-form-item label="이메일">
          <el-input v-model="state.login.email"></el-input>
        </el-form-item>

        <el-form-item label="비밀번호">
          <el-input type="password" v-model="state.login.password"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="doLogin()">로그인</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<style scoped lang="scss"></style>