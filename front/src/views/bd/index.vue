<template>
  <el-row>
    <el-col :md="4" :xs="0">&zwj;</el-col>
    <el-col :md="16" :xs="24">
      <h1 class="my-8 text-lg text-center">{{ baseInfo.title }}</h1>
      <parsePage ref="ref_parsePage" v-on:upSharePageInfo="getSharePageInfo" />

      <fileListPage
        v-show="
          Boolean(sharePageInfo.file_list) && sharePageInfo.file_list.length > 0
        "
        ref="ref_fileListPage"
        :share-page-prop="sharePageInfo"
        v-on:reParse="reParse"
      ></fileListPage>
    </el-col>
  </el-row>
</template>

<script>
import parsePage from "./page/parsePage";
import fileListPage from "./page/fileListPage";

export default {
  name: "BDIndex",
  components: { parsePage, fileListPage },
  data() {
    return {
      sharePageInfo: {
        file_list: [],
      },
    };
  },

  computed: {
    baseInfo: {
      get() {
        return this.$store.state.bd.baseInfo;
      },
    },
  },
  methods: {
    getSharePageInfo(info) {
      this.sharePageInfo = info;
      this.$refs.ref_fileListPage.breadCrumb = [];
    },

    reParse() {
      this.$refs.ref_parsePage.parse();
    },
  },
};
</script>

<style scoped></style>
