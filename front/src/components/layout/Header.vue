<template>
  <div class="header">
    <div class="header-wrapper">
      <router-link to="/">
        <div class="flex items-center">
          <Logo class="h-10 w-10" />
          <p class="pl-3 select-none">{{ title }}</p>
        </div>
      </router-link>
    </div>
  </div>
</template>

<script>
import Logo from "./Logo.vue";
import { info } from "@/api/bd";

export default {
  components: {
    Logo,
  },
  computed: {
    title: {
      get() {
        return this.$store.state.bd.baseInfo.title;
      },
    },
  },
  created() {
    this.initWebInfo();
  },
  methods: {
    initWebInfo() {
      info().then((resp) => {
        this.$store.commit("bd/setBaseInfo", resp.data);
      });
    },
  },
};
</script>

<style scoped>
.header {
  @apply h-16 drop-shadow-sm shadow-md bg-white;
}

.header-wrapper {
  @apply w-11/12 max-w-5xl h-full m-auto font-bold text-2xl flex items-center justify-between;
}
</style>
