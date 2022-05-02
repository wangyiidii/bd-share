<template>
  <div>
    <div class="mt-4 mb-2 text-center text-xs text-gray-400">
      部分内容来自互联网，仅供学习使用，与本站无关，侵删
    </div>
    <div
      class="text-center text-xs text-gray-400"
      v-html="baseInfo.footer"
    ></div>
  </div>
</template>

<script>
export default {
  name: "Footer",
  computed: {
    baseInfo: {
      get() {
        return this.$store.state.bd.baseInfo;
      },
    },
  },
  methods: {},
};
</script>

<style>
.footer {
  text-align: center;
  color: #909399;
  font-size: 10px;
}
</style>
