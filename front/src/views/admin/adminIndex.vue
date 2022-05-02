<template>
  <el-row>
    <el-col :md="4" :xs="0">&zwj;</el-col>
    <el-col :md="16" :xs="24">
      <h1 class="my-8 text-lg text-center">后台管理</h1>
      <SysConfig />
      <cdkManage />
      <svipInfoManage />
    </el-col>
  </el-row>
</template>

<script>
import SysConfig from "./page/sysConfig";
import cdkManage from "./page/cdkManage";
import svipInfoManage from "./page/svipInfoManage";

export default {
  name: "AdminIndex",
  components: { SysConfig, cdkManage, svipInfoManage },
};
</script>

<style scoped></style>
