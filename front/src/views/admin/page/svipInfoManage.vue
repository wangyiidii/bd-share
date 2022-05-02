<template>
  <el-row>
    <el-col class="p-4">
      <div class="border-dashed border-2 p-2 border-light-blue-500 rounded-sm">
        <div
          class="pl-2 py-2 mb-2 bg-gray-50 rounded-sm border-l-4 border-red-500"
        >
          SVIP信息管理
        </div>
        <div>
          <el-button
            class="float-right"
            type="primary"
            size="mini"
            @click="handleAddOrEdit"
            >添加SVIP
          </el-button>
        </div>
        <el-table :data="svipInfos" stripe style="width: 100%">
          <el-table-column prop="alias" label="名称" width="180" />

          <el-table-column prop="ua" label="UA" width="180">
            <template #default="scope">
              <el-tag size="mini">{{ scope.row.ua }}</el-tag>
            </template>
          </el-table-column>

          <el-table-column prop="status" label="状态">
            <template #default="scope">
              <el-tag
                size="mini"
                type="success"
                v-if="scope.row.status === 'ENABLED'"
              >
                可用
              </el-tag>
              <el-tag
                size="mini"
                type="success"
                v-if="scope.row.status === 'DISABLED'"
              >
                禁用
              </el-tag>
              <el-tag
                size="mini"
                type="success"
                v-if="scope.row.status === 'DELETED'"
              >
                已删除
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180">
            <template slot-scope="scope">
              {{ formatDateTime(scope.row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="updateTime" label="更新时间" width="180">
            <template slot-scope="scope">
              {{ formatDateTime(scope.row.updateTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="240px">
            <template #default="scope">
              <el-button
                size="mini"
                @click="handleAddOrEdit(scope.$index, scope.row)"
                >编辑
              </el-button>
              <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)"
                >删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- dialog -->
      <div>
        <el-dialog
          :visible.sync="addOrEditSvipInfo.dialogVisible"
          :title="addOrEditSvipInfo.data.id ? '编辑SVIP' : '新增SVIP'"
          :width="addOrEditSvipInfo.dialogWidth"
        >
          <div>
            <el-form
              ref="form"
              :model="addOrEditSvipInfo.data.cdk"
              label-width="100px"
            >
              <el-form-item label="名称">
                <el-input v-model="addOrEditSvipInfo.data.alias"></el-input>
              </el-form-item>
              <el-form-item label="UA">
                <el-input v-model="addOrEditSvipInfo.data.ua"></el-input>
              </el-form-item>
              <el-form-item label="BDUSS">
                <el-input v-model="addOrEditSvipInfo.data.bduss"></el-input>
              </el-form-item>
              <el-form-item label="STOKEN">
                <el-input v-model="addOrEditSvipInfo.data.stoken"></el-input>
              </el-form-item>
            </el-form>
          </div>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="closeCdkDialog" size="mini">关闭</el-button>
              <el-button
                type="primary"
                v-if="!addOrEditSvipInfo.data.id"
                @click="handleAddSvip"
                size="mini"
                >添加</el-button
              >
              <el-button
                type="primary"
                v-else
                @click="handleUpdateSvip"
                size="mini"
                >保存</el-button
              >
            </span>
          </template>
        </el-dialog>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import {
  getSvipInfoList,
  addSvipInfo,
  updateSvipInfo,
  delSvipBatch,
} from "../../../api/admin";
import Moment from "moment";

export default {
  name: "cdkManage",
  mounted() {
    // 监听窗口变化
    window.onresize = () => {
      return (() => {
        this.setDialogWidth();
      })();
    };
  },
  data() {
    return {
      svipInfos: [],
      addOrEditSvipInfo: {
        dialogVisible: false,
        data: { alias: "", ua: "USER_AGENT" },
        dialogWidth: "800px",
      },
    };
  },
  created() {
    this.getSvipList();
  },
  methods: {
    getSvipList() {
      getSvipInfoList()
        .then((resp) => {
          this.svipInfos = resp.data.records;
        })
        .catch(() => {});
    },
    setDialogWidth() {
      let val = document.body.clientWidth;
      const def = 1000; // 默认宽度
      if (val < def) {
        this.addOrEditSvipInfo.dialogWidth = "80%";
      } else {
        this.addOrEditSvipInfo.dialogWidth = "50%";
      }
    },
    formatDateTime(val) {
      return Moment(val * 1000).format("YYYY-MM-DD HH:mm:ss");
    },
    closeCdkDialog() {
      this.addOrEditSvipInfo.data = { id: null, type: "TIMES", maxTimes: 3 };
      this.addOrEditSvipInfo.dialogVisible = false;
    },
    handleAddSvip() {
      addSvipInfo(this.addOrEditSvipInfo.data).then((resp) => {
        this.$message.success(resp.msg);
        this.addOrEditSvipInfo.dialogVisible = false;
        this.getSvipList();
      });
    },
    handleUpdateSvip() {
      updateSvipInfo(this.addOrEditSvipInfo.data).then((resp) => {
        this.$message.success(resp.msg);
        this.addOrEditSvipInfo.dialogVisible = false;
        this.getSvipList();
      });
    },
    handleAddOrEdit(index, row) {
      if (row) {
        this.addOrEditSvipInfo.data = row;
      } else {
        this.addOrEditSvipInfo.data = { id: null, type: "TIMES", maxTimes: 3 };
      }
      this.addOrEditSvipInfo.dialogVisible = true;
    },
    handleDelete(index, row) {
      this.$confirm("此操作将永久删除SVIP信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        let delIds = [row.id];
        delSvipBatch(delIds).then((resp) => {
          this.$message.success(resp.msg);
          this.getSvipList();
        });
      });
    },
    formatTableDateTime(row, column) {
      return Moment(row.createTime).format("YYYY-MM-DD HH:mm:ss");
    },
  },
};
</script>

<style scoped></style>
