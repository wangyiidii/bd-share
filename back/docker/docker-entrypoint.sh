#!/bin/bash
set -e

if [ ! -s ${BD_DIR}/db/data.db ]; then
  echo "检测到无数据库文件，从示例文件复制一份用于初始化...\n"
  cp -fv ${BD_DIR}/sample/data.db ${BD_DIR}/db/data.db
fi

nohup java -jar ${BD_DIR}/app.jar --server.port=8081 --spring.profiles.active=prod >${BD_DIR}/logs/console/all.log 2>&1 &
cd /usr/sbin
# 前台启动nginx，不守护在后台运行，解决docker Exited (0)
nginx -g 'daemon off;'

exec "$@"
