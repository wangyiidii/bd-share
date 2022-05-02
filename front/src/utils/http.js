// 导入封装好的axios实例
import request from "./request";

const http = {
  /**
   * methods: 请求
   * @param url 请求地址
   * @param params 请求参数
   */
  get(url, params) {
    const config = {
      method: "get",
      url: url,
    };
    if (params) config.params = params;
    return request(config);
  },
  postForm(url, params) {
    const config = {
      method: "post",
      url: url,
      headers: {
        "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
      },
    };
    if (params) config.data = params;
    return request(config);
  },
  postJson(url, params) {
    const config = {
      method: "post",
      url: url,
      headers: { "Content-Type": "application/json" },
    };
    if (params) config.data = params;
    return request(config);
  },
  put(url, params) {
    const config = {
      method: "put",
      url: url,
    };
    if (params) config.params = params;
    return request(config);
  },
  putJson(url, params) {
    const config = {
      method: "put",
      url: url,
    };
    if (params) config.data = params;
    return request(config);
  },
  delete(url, params) {
    const config = {
      method: "delete",
      url: url,
    };
    if (params) config.params = params;
    return request(config);
  },
  deleteJson(url, params) {
    const config = {
      method: "delete",
      url: url,
    };
    if (params) config.data = params;
    return request(config);
  },
};
//导出
export default http;
