<template>
  <el-row>
    <el-col class="p-4">
      <div class="border-dashed border-2 p-2 border-light-blue-500 rounded-sm">
        <div
          class="pl-2 py-2 mb-2 bg-gray-50 rounded-sm border-l-4 border-yellow-500"
        >
          系统管理
        </div>
        <el-form ref="form" :model="sysConfig" label-width="100px">
          <el-form-item label="用户名">
            <el-input v-model="sysConfig.username"></el-input>
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="sysConfig.password"></el-input>
          </el-form-item>
          <el-form-item label="SVIP切换模式">
            <el-radio-group v-model="sysConfig.svipSwitchMode" size="mini">
              <el-radio-button label="SINGLETON">固定一个</el-radio-button>
              <el-radio-button label="RANDOM">随机</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="免费次数">
            <el-input-number
              v-model="sysConfig.freeTimes"
              size="mini"
              @change="handleChangeFreeTimes"
            />
          </el-form-item>
          <el-form-item label="网站标题">
            <el-input v-model="sysConfig.title"></el-input>
          </el-form-item>
          <el-form-item label="网站footer">
            <el-input
              type="textarea"
              :row="5"
              v-model="sysConfig.footer"
              placeholder="支持html"
            ></el-input>
          </el-form-item>
          <el-form-item label="CDK说明">
            <el-input
              type="textarea"
              :row="5"
              v-model="sysConfig.cdkNotice"
            ></el-input>
          </el-form-item>
          <div class="text-center">
            <el-button type="primary" size="mini" @click="updateSysConfig()"
              >保存</el-button
            >
          </div>
        </el-form>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import { getSysConfig, updateSysConfig } from "../../../api/admin";
export default {
  name: "SysConfig",
  data() {
    return {
      sysConfig: {
        username: "",
        password: "",
        svipSwitchMode: "",
        freeTimes: 3,
        title: "",
        footer: "",
        cdkNotice: "",
      },
    };
  },
  created() {
    this.getSysConfig();
  },
  methods: {
    getSysConfig() {
      getSysConfig().then((resp) => {
        this.sysConfig = resp.data;
      });
    },
    updateSysConfig() {
      updateSysConfig(this.sysConfig)
        .then((resp) => {
          // localStorage.removeItem("token");
          this.$message.success(resp.msg);

          let that = this;
          setTimeout(() => {
            that.getSysConfig();
          }, 1000);
        })
        .catch((e) => {
          console.log(e);
        })
        .finally(() => {});
    },
    handleChangeFreeTimes(v) {
      this.sysConfig.freeTimes = v;
    },
  },
};
</script>

<style scoped></style>
