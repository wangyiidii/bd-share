import http from "@/utils/http";

export function getSysConfig() {
  return http.get("/admin/sysConfig");
}

export function updateSysConfig(params) {
  return http.postJson("/admin/sysConfig", params);
}

export function login(params) {
  return http.postJson("/admin/login", params);
}

// cdk
export function getCdkList(params) {
  return http.get("/admin/cdk/list", params);
}

export function addCdk(params) {
  return http.postJson("/admin/cdk", params);
}

export function updateCdk(params) {
  return http.putJson("/admin/cdk", params);
}

export function delCdkBatch(params) {
  return http.deleteJson("/admin/cdk/del", params);
}

// svip
export function getSvipInfoList(params) {
  return http.get("/admin/svipInfo/list", params);
}

export function updateSvipInfo(params) {
  return http.putJson("/admin/svipInfo", params);
}

export function addSvipInfo(params) {
  return http.postJson("/admin/svipInfo", params);
}

export function delSvipBatch(params) {
  return http.deleteJson("/admin/svipInfo/del", params);
}
