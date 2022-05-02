<template>
  <el-row>
    <el-col class="p-4">
      <div class="border-dashed border-2 p-2 border-light-blue-500 rounded-sm">
        <div
          class="pl-2 py-2 mb-2 bg-gray-50 rounded-sm border-l-4 border-green-500"
        >
          CDK管理
        </div>
        <div class="my-2">
          <el-button
            class="float-right"
            type="primary"
            size="mini"
            @click="popAddOrEdit"
            >添加CDK
          </el-button>
        </div>
        <el-table :data="cdks" stripe style="width: 100%">
          <el-table-column prop="cdk" label="CDK" width="180" />
          <el-table-column label="CDK类型">
            <template #default="scope">
              <el-tag
                size="mini"
                type="warning"
                v-if="scope.row.type === 'TIMES'"
                >次数
              </el-tag>
              <el-tag
                size="mini"
                type="warning"
                v-if="scope.row.type === 'PERIOD'"
                >时间
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="剩余次数/到期时间" width="200">
            <template #default="scope">
              <el-tag size="mini" v-if="scope.row.type === 'TIMES'">
                {{ scope.row.times }}
              </el-tag>
              <el-tag size="mini" v-if="scope.row.type === 'PERIOD'">
                {{ formatDateTime(scope.row.expireTime) }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column prop="remark" label="备注"> </el-table-column>

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
                @click="popAddOrEdit(scope.$index, scope.row)"
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
          :visible.sync="addOrEditCdk.dialogVisible"
          :title="addOrEditCdk.data.id ? '编辑CDK' : '新增CDK'"
          :width="addOrEditCdk.dialogWidth"
        >
          <div>
            <el-form ref="form" label-width="100px">
              <el-form-item label="CDK">
                <el-input v-model="addOrEditCdk.data.cdk"></el-input>
              </el-form-item>
              <el-form-item label="CDK类型">
                <el-radio-group v-model="addOrEditCdk.data.type" size="mini">
                  <el-radio-button label="TIMES">次数</el-radio-button>
                  <el-radio-button label="PERIOD">时间</el-radio-button>
                </el-radio-group>
              </el-form-item>
              <el-form-item
                label="次数"
                v-if="addOrEditCdk.data.type == 'TIMES'"
              >
                <el-input-number
                  v-model="addOrEditCdk.data.times"
                  size="mini"
                  :min="0"
                />
              </el-form-item>
              <el-form-item label="到期时间" v-else>
                <el-date-picker
                  v-model="addOrEditCdk.data.expireTime"
                  type="datetime"
                  placeholder="请选择时间"
                  size="mini"
                >
                </el-date-picker>
              </el-form-item>
              <el-form-item label="备注">
                <el-input v-model="addOrEditCdk.data.remark"></el-input>
              </el-form-item>
            </el-form>
          </div>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="closeCdkDialog" size="mini">关闭</el-button>
              <el-button type="primary" @click="handleAddOrEditCdk" size="mini"
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
import { getCdkList, addCdk, updateCdk, delCdkBatch } from "../../../api/admin";
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
      cdks: [],
      addOrEditCdk: {
        dialogVisible: false,
        data: {
          id: null,
          cdk: "",
          type: "TIMES",
          times: 0,
          remark: "",
          expireTime: null,
        },
        dialogWidth: "800px",
      },
    };
  },
  created() {
    this.getCdkList();
  },
  methods: {
    getCdkList() {
      getCdkList()
        .then((resp) => {
          this.cdks = resp.data.records;
        })
        .catch(() => {});
    },
    setDialogWidth() {
      let val = document.body.clientWidth;
      const def = 1000; // 默认宽度
      if (val < def) {
        this.addOrEditCdk.dialogWidth = "80%";
      } else {
        this.addOrEditCdk.dialogWidth = "50%";
      }
    },
    formatDateTime(val) {
      return Moment(val * 1000).format("YYYY-MM-DD HH:mm:ss");
    },
    closeCdkDialog() {
      this.addOrEditCdk.data = { id: null, type: "TIMES", times: 3 };
      this.addOrEditCdk.dialogVisible = false;
    },
    popAddOrEdit(index, row) {
      if (row) {
        this.addOrEditCdk.data = row;
        console.log(this.addOrEditCdk.data.expireTime);
        if (this.addOrEditCdk.data.expireTime.toString().length == 10) {
          this.addOrEditCdk.data.expireTime =
            this.addOrEditCdk.data.expireTime * 1000;
        }
      } else {
        this.addOrEditCdk.data = { id: null, type: "TIMES", times: 3 };
      }
      this.addOrEditCdk.dialogVisible = true;
    },
    handleAddOrEditCdk() {
      let data = this.addOrEditCdk.data;
      data.expireTime = data.expireTime / 1000;
      if (!this.addOrEditCdk.data.id) {
        addCdk(data).then((resp) => {
          this.$message.success(resp.msg);
          this.addOrEditCdk.dialogVisible = false;
          this.getCdkList();
        });
      } else {
        updateCdk(data).then((resp) => {
          this.$message.success(resp.msg);
          this.addOrEditCdk.dialogVisible = false;
          this.getCdkList();
        });
      }
    },
    handleDelete(index, row) {
      this.$confirm("此操作将永久删除CDK 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        let delIds = [row.id];

        delCdkBatch(delIds).then((resp) => {
          this.$message.success(resp.msg);
          this.getCdkList();
        });
      });
    },
  },
};
</script>

<style scoped></style>
