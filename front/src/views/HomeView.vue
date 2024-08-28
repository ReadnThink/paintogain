<script setup lang="ts">
import Feed from '@/components/Feed.vue'
</script>
<template>
  <div class="content">
    <ul class="feeds">
      <li>
        <Feed />
      </li>
      <li>
        <Feed />
      </li>
      <li>
        <Feed />
      </li>
      <li>
        <Feed />
      </li>
    </ul>
  </div>
</template>

<style scoped lang="scss">
.content {
  height: calc(100vh - 60px - 2rem - 20px - 1.5rem);
  padding: 0 1rem 0 1rem;
}

.feeds {
  list-style: none;
  padding: 0;

  li {
    margin-bottom: 2.4rem;

    &:last-child {
      margin-bottom: 0;
    }
  }
}
</style>