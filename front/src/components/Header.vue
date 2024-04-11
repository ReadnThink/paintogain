<template>
  <el-header class="">

    <!--      <RouterLink to="/">Home</RouterLink>-->
    <!--      <RouterLink to="/write">피드 작성</RouterLink>-->
    <el-menu mode="horizontal" router>
      <el-menu-item index="/">Home</el-menu-item>
      <el-menu-item index="/write">피드 작성</el-menu-item>
    </el-menu>
  </el-header>
</template>

<script setup lang="ts"> </script>

<style scoped>
header {
  padding: 0;
  height: 60px;
}
</style>