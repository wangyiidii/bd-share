<template>
  <el-row>
    <el-col class="p-4">
      <el-form ref="form" :model="loginForm" label-width="60px">
        <el-form-item label="用户名">
          <el-input v-model="loginForm.username"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="loginForm.password"></el-input>
        </el-form-item>
        <div class="text-center">
          <el-button
            type="primary"
            size="mini"
            @click="login()"
            :loading="loginLoading"
            >登录</el-button
          >
        </div>
      </el-form>
    </el-col>
  </el-row>
</template>

<script>
import { login } from "../../../api/admin";
export default {
  name: "login",
  data() {
    return {
      loginForm: { username: "", password: "" },
      loginLoading: false,
    };
  },
  methods: {
    login() {
      this.loginLoading = true;
      login(this.loginForm).then((resp) => {
        this.loginLoading = false;
        localStorage.setItem("token", resp.data.token);
        setTimeout(() => {
          this.$router.push({
            path: "/admin/config",
            params: {},
          });
        }, 500);
      });
    },
  },
};
</script>

<style scoped></style>
