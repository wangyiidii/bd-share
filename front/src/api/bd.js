import http from "@/utils/http";

export function info() {
  return http.get("/info");
}

export function parse(params) {
  return http.get("/parse", params);
}

export function openDir(params) {
  return http.postJson("/openDir", params);
}

export function downloadUrl(params) {
  return http.postJson("/downloadUrl", params);
}
