<template>
  <div class="p-4">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <el-avatar
            class="inline-block align-middle"
            :src="sharePageProp.share_photo"
          />
          <span class="inline-block align-middle font-bold ml-2">
            {{ sharePageProp.linkusername }} 分享的文件
          </span>
        </div>
      </template>

      <el-breadcrumb separator=">">
        <el-breadcrumb-item @click="reParse">
          <span class="text-xs">全部文件</span>
        </el-breadcrumb-item>
        <el-breadcrumb-item
          v-for="(bread, index) in breadCrumb"
          :key="bread.fs_id"
        >
          <el-link
            type="primary"
            v-if="index != breadCrumb.length"
            :underline="false"
            @click="handleSwitch(index)"
          >
            <span class="text-xs w-4 truncate">
              {{ bread.server_filename }}</span
            >
          </el-link>
        </el-breadcrumb-item>
      </el-breadcrumb>

      <div class="text-center">
        <el-table
          class="w-full"
          :data="sharePageProp.file_list"
          v-loading="fileListLoading"
        >
          <el-table-column label="文件名" width="240px">
            <template #default="scope">
              <span class="inline-block align-middle">
                <i v-if="scope.row.isdir == 1" class="el-icon-folder-opened" />
                <i v-else class="el-icon-files" />
              </span>
              <el-tooltip
                effect="dark"
                :content="scope.row.server_filename"
                placement="top"
              >
                <span
                  class="inline-block align-middle truncate w-48 ml-2 text-sm"
                >
                  {{ scope.row.server_filename }}
                </span>
              </el-tooltip>
            </template>
          </el-table-column>
          <!--          prop="size"-->
          <el-table-column
            label="大小"
            width="100px"
            :formatter="formatFileSize"
          >
            <template #default="scope">
              <div class="text-sm">{{ formatFileSize(scope.row.size) }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="server_mtime" label="修改日期" width="150px">
            <template #default="scope">
              <div class="text-sm">{{ formatTableDateTime(scope.row) }}</div>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            width="200px"
            :formatter="formatFileSize"
          >
            <template #default="scope">
              <el-button
                type="primary"
                size="mini"
                v-if="scope.row.isdir == 1"
                @click="openDir(scope.row)"
                >打开</el-button
              >
              <el-button
                type="success"
                size="mini"
                v-else
                @click="showCdkDialog(scope.row)"
                icon="el-icon-download"
              >
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <!-- 下载信息dialog -->
    <el-dialog
      :visible.sync="dialog_downloadUrl_visible"
      title="获取成功"
      :width="dialogWidth"
    >
      <div>
        <div class="my-2">
          <span class="inline-block align-middle">
            <i class="el-icon-files" /> 名称：
          </span>
          <span class="ml-4 inline-block align-middle">{{
            currFile.server_filename
          }}</span>
        </div>

        <div class="my-2">
          <span class="inline-block align-middle">
            <i class="el-icon-info" /> 大小：
          </span>
          <span class="ml-4 inline-block align-middle">{{
            formatFileSize(currFile.size)
          }}</span>
        </div>

        <div class="my-2">
          <span class="inline-block align-middle">
            <i class="el-icon-link" /> 直链：
          </span>
          <span class="ml-4 inline-block align-middle">
            <el-button @click="copy(currFile.url)" size="mini" type="text"
              >点击复制下载链接</el-button
            >
          </span>
        </div>

        <div class="my-2">
          <span class="inline-block align-middle">
            <i class="el-icon-orange" /> UA：
          </span>
          <span class="ml-4 inline-block align-middle">
            <el-tag>{{ currFile.ua }}</el-tag>
          </span>
        </div>

        <div
          class="my-4 mx-2 p-4 bg-gray-50 rounded-sm border-l-4 border-yellow-500"
        >
          <p class="my-2 font-bold">注意:</p>
          <ul class="list-disc ml-4 text-gray-400">
            <li>下载后请自行替换UA使用</li>
            <li>在解析后的8小时内, 再次解析不消耗次数</li>
          </ul>
        </div>
      </div>
    </el-dialog>

    <!-- cdk dialog-->
    <el-dialog
      title="请输入cdk"
      :visible.sync="dialog_cdk_input_visible"
      :width="dialogWidth"
    >
      <el-input placeholder="请输入cdk" v-model="cdk"></el-input>
      <div class="mt-2">
        {{ baseInfo.cdkNotice }}
      </div>
      <div class="mt-4 float-right">
        <el-button size="mini" @click="dialog_cdk_input_visible = false"
          >取 消</el-button
        >
        <el-button size="mini" type="primary" @click="getDownloadUrl()"
          >确 定
        </el-button>
      </div>
      <div class="clear-right"></div>
    </el-dialog>
  </div>
</template>
<script>
import Moment from "moment";
import { openDir, downloadUrl } from "@/api/bd.js";
import { handleClipboard } from "@/utils/clipboard.js";
import { mapGetters } from "vuex";

export default {
  name: "fileListPage",
  props: {
    sharePageProp: {
      type: Object,
      default: function () {
        return {};
      },
    },
  },
  computed: {
    ...mapGetters({
      getCdk: "bd/getCdk",
    }),
    baseInfo: {
      get() {
        return this.$store.state.bd.baseInfo;
      },
    },
  },
  data() {
    return {
      breadCrumb: [],
      fileListLoading: false,
      dialog_cdk_input_visible: false,
      dialog_downloadUrl_visible: false,
      currFile: {},
      dialogWidth: "80%",
      cdk: "",
      tmp: null,
    };
  },
  mounted() {
    // 监听窗口变化
    window.onresize = () => {
      return (() => {
        this.setDialogWidth();
      })();
    };
  },
  created() {
    this.setDialogWidth();
  },
  methods: {
    formatTableDateTime(row) {
      return Moment(row.server_mtime * 1000).format("YYYY-MM-DD HH:mm:ss");
    },
    formatFileSize(fileSize) {
      if (fileSize == 0) {
        return "-";
      }
      let unit = ["B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"];
      let i = Math.floor(Math.log(fileSize) / Math.log(1024));
      return (fileSize / Math.pow(1024, i)).toPrecision(3) + " " + unit[i];
    },
    reParse() {
      this.breadCrumb = [];
      this.$emit("reParse");
    },
    openDir(file) {
      let param = {
        bdstoken: this.sharePageProp.bdstoken,
        bdclnd: this.sharePageProp.bdclnd,
        shareid: this.sharePageProp.shareid,
        share_uk: this.sharePageProp.share_uk,
        app_id: file.app_id,
        fs_id: file.fs_id,
        isdir: file.isdir,
        path: file.path,
      };
      this.fileListLoading = true;
      openDir(param)
        .then((resp) => {
          this.$props.sharePageProp.file_list = resp.data;
          this.breadCrumb.push(file);
        })
        .catch((e) => {
          console.log(e);
        })
        .finally(() => {
          this.fileListLoading = false;
        });
    },
    showCdkDialog(file) {
      this.dialog_cdk_input_visible = true;
      this.tmp = file;
    },
    getDownloadUrl() {
      if (!this.cdk) {
        this.$message.error("请输入cdk");
        return;
      }
      this.$store.commit("bd/setCdk", this.cdk);
      this.dialog_cdk_input_visible = true;
      let file = this.tmp;

      let param = {
        bdstoken: this.sharePageProp.bdstoken,
        bdclnd: this.sharePageProp.bdclnd,
        shareid: this.sharePageProp.shareid,
        share_uk: this.sharePageProp.share_uk,
        app_id: file.app_id,
        fs_id: file.fs_id,
        isdir: file.isdir,
        path: file.path,
      };
      this.fileListLoading = true;
      downloadUrl(param)
        .then((resp) => {
          this.currFile = file;
          this.currFile.url = resp.data.url;
          this.currFile.ua = resp.data.ua;
          this.dialog_downloadUrl_visible = true;
        })
        .catch(() => {})
        .finally(() => {
          this.fileListLoading = false;
          this.dialog_cdk_input_visible = false;
        });
    },
    handleSwitch(index) {
      let clickFile = this.breadCrumb[index];
      this.breadCrumb = this.breadCrumb.slice(0, index);
      this.openDir(clickFile);
    },
    copy(val) {
      handleClipboard(
        val,
        event,
        () => {
          this.$message.success("复制成功");
        },
        () => {
          this.$message.error(this.$t("复制失败"));
        }
      );
    },
    setDialogWidth() {
      let val = document.body.clientWidth;
      const def = 1000; // 默认宽度
      if (val < def) {
        this.dialogWidth = "80%";
      } else {
        this.dialogWidth = "50%";
      }
    },
  },
};
</script>

<style scoped></style>
