import { baseUrl } from "../../config/env";

import axios from "axios";
import Message from "@/utils/message.js";
import store from "@/store/index.js";
import router from "@/router/index";
import da from "element-ui/src/locale/lang/da";

// 1. 创建新的axios实例
const service = axios.create({
  baseURL: baseUrl,
  timeout: 60 * 1000,
});
// 2.请求拦截器
service.interceptors.request.use(
  (config) => {
    //发请求前做的一些处理，数据转化，配置请求头，设置token,设置loading等
    let token = localStorage.getItem("token");
    if (token) {
      config.headers["token"] = token;
    }
    config.headers["cdk"] = encodeURIComponent(store.state.bd.cdk);
    return config;
  },
  (error) => {
    console.log(error);
    Promise.reject(error);
  }
);

// 3.响应拦截器
service.interceptors.response.use(
  (response) => {
    // 接收到响应数据并成功后的一些共有的处理，关闭loading等
    const data = response.data;
    let msg = data.msg;
    let bizCode = data.code;
    // admin登录拦截
    if (bizCode == 10007) {
      setTimeout(() => {
        router.push({
          path: "/admin/login",
          params: {},
        });
      }, 500);
      return;
    }
    // 忽略
    if (bizCode != 0) {
      Message.error(msg);
      return;
    }

    return response.data;
  },
  (error) => {
    const resp = error.response.data;
    let msg = resp.msg;
    if (msg) {
      Message.error(msg);
    }

    return Promise.reject(error);
  }
);
//4.导入文件
export default service;
