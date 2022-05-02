import { Notification } from "element-ui";

export default {
  success(title, message) {
    if (!title) title = "提示";
    Notification({
      title: title,
      message: message,
      type: "success",
    });
  },
  warning(title, message) {
    if (!title) title = "提示";
    Notification({
      title: title,
      message: message,
      type: "warning",
    });
  },
  info(title, message) {
    if (!title) title = "提示";
    Notification({
      title: title,
      message: message,
      type: "info",
    });
  },
  error(title, message) {
    if (!title) title = "提示";
    Notification({
      title: title,
      message: message,
      type: "error",
    });
  },
  notifyHtml(title, message) {
    if (!title) title = "提示";
    Notification({
      title: title,
      dangerouslyUseHTMLString: true,
      message: message,
    });
  },
};
