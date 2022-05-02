<template>
  <el-row>
    <el-col :md="4" :xs="0">&zwj;</el-col>
    <el-col :md="16" :xs="24">
      <h1 class="my-8 text-lg text-center">后台登录</h1>
      <login />
    </el-col>
  </el-row>
</template>

<script>
import Login from "./page/login";
export default {
  name: "AdminLogin",
  components: { Login },
};
</script>

<style scoped></style>
