<template>
  <div id="app">
    <Header />
    <transition name="el-fade-in-linear">
      <router-view />
    </transition>
    <Footer />
    <el-backtop></el-backtop>
  </div>
</template>
<script>
import Footer from "@/components/layout/Footer";
import Header from "@/components/layout/Header";

export default {
  name: "APP",
  components: {
    Header,
    Footer,
  },
};
</script>
<style>
#app {
  font-family: "Source Sans Pro", -apple-system, BlinkMacSystemFont, "Segoe UI",
    Roboto, "Helvetica Neue", Arial, sans-serif;
  font-size: 16px;
  word-spacing: 1px;
  -ms-text-size-adjust: 100%;
  -webkit-text-size-adjust: 100%;
  -moz-osx-font-smoothing: grayscale;
  -webkit-font-smoothing: antialiased;
  box-sizing: border-box;
}
</style>
