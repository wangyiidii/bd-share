import Vue from "vue";
import VueRouter from "vue-router";
import BDIndex from "@/views/bd";
import AdminLogin from "@/views/admin/adminLogin";
import AdminIndex from "@/views/admin/adminIndex";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "BDIndex",
    component: BDIndex,
  },
  {
    path: "/admin",
    redirect: "/admin/login",
  },
  {
    path: "/admin/login",
    name: "AdminLogin",
    component: AdminLogin,
  },
  {
    path: "/admin/config",
    name: "AdminIndex",
    component: AdminIndex,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
