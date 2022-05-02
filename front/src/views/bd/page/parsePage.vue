<template>
  <div class="p-4">
    <el-form
      label-position="left"
      label-width="64px"
      :model="form"
      :rules="rules"
      ref="ref_form"
    >
      <el-form-item label="链接" prop="url">
        <el-input
          v-model="form.url"
          placeholder="https://pan.baidu.com/s/xxxxxxxxxx"
        ></el-input>
      </el-form-item>
      <el-form-item label="提取码" prop="pwd">
        <el-input v-model="form.pwd"></el-input>
      </el-form-item>
    </el-form>
    <div class="text-center">
      <el-button
        type="primary"
        size="mini"
        :loading="parseLoading"
        @click="parse()"
      >
        解析
      </el-button>
      <div class="mt-2">
        <el-link style="font-size: 8px" type="info" @click="helpVisible = true"
          >使用指南
        </el-link>
      </div>
    </div>

    <el-dialog title="使用指南" :visible.sync="helpVisible" width="80%">
      <div>
        <div>
          <div class="text-lg font-black">电脑端:</div>
          <div>
            <div class="pl-2">方式一：Motrix（推荐，mac win通用）</div>
            <div class="pl-4">
              <div>
                1. 官网下载
                <el-link
                  icon="el-icon-link"
                  :underline="true"
                  href="https://motrix.app/"
                  >Motrix
                </el-link>
              </div>
              <div>2. 复制下载链接</div>
              <div>3. 勾选高级选项，配置User-Agent</div>
            </div>
          </div>
          <div class="mt-4">
            <div class="pl-2">方式二 ：IDM</div>
            <div class="pl-4">
              <div>
                1. 下载
                <el-link
                  icon="el-icon-link"
                  :underline="true"
                  href="https://wwx.lanzoux.com/iPdG0gnpuod"
                  >IDM</el-link
                >
              </div>
              <div>
                2. 选项 -> 下载 -> 手动添加任务时使用的用户代理（UA）->
                填入页面上的UA
              </div>
              <div>3. 在 IDM 新建任务，粘贴链接即可下载</div>
            </div>
          </div>
        </div>

        <el-divider></el-divider>

        <div>
          <div class="text-lg font-black">手机端</div>
          <div class="mt-4">
            <div class="pl-2">方式一 ：ADM（安卓推荐）</div>
            <div class="pl-4">
              <div>
                1. 下载
                <el-link
                  icon="el-icon-link"
                  :underline="true"
                  href="https://wwx.lanzoux.com/i1Iurgnq0kf"
                  >ADM</el-link
                >
              </div>
              <div>
                2. 设置 -> 下载中 -> 浏览器标识 -> 自定义
                浏览器标识，填入页面上的UA
              </div>
              <div>3. 在 IDM 新建任务，粘贴链接即可下载</div>
            </div>
          </div>

          <div class="mt-4">
            <div class="pl-2">方式二 ：ALook浏览器（IOS推荐）</div>
            <div class="pl-4">
              <div>1. 下载ALook浏览器</div>
              <div>
                2. 设置 -> 通用设置 -> 浏览器标识 -> 移动版浏览器标识 -> 自定义
                -> 填入页面上的UA
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { parse } from "@/api/bd.js";

export default {
  name: "parsePage",
  data() {
    const checkUrl = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("请输入分享链接"));
      } else {
        return callback();
      }
    };
    return {
      form: {
        url: "",
        pwd: "",
      },
      rules: {
        url: [{ validator: checkUrl, trigger: "blur" }],
      },
      parseLoading: false,
      helpVisible: false,
    };
  },
  methods: {
    parse() {
      this.$refs["ref_form"].validate((valid) => {
        if (valid) {
          this.parseLoading = true;
          this.$emit("upSharePageInfo", {});
          parse(this.form)
            .then((resp) => {
              this.$emit("upSharePageInfo", resp.data);
            })
            .catch(() => {})
            .finally(() => {
              this.parseLoading = false;
            });
        } else {
          return false;
        }
      });
    },
  },
};
</script>

<style scoped></style>
