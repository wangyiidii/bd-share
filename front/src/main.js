import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store/index";
import "tailwindcss/tailwind.css";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import notify from "./utils/notify.js";
import message from "./utils/message.js";

Vue.use(ElementUI);

Vue.config.productionTip = false;

Vue.config.$notify = notify;
Vue.config.$message = message;

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
