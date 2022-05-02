import Vue from "vue";
import Vuex from "vuex";
import bd from "./module/bd/index";
Vue.use(Vuex);

export default new Vuex.Store({
  state: {},
  mutations: {},
  actions: {},
  modules: { bd },
});
