#!/bin/bash

CONTAINER_NAME="mysql-container"

echo "========== 启动podman和myslq脚本 =========="

# 检查 Podman machine
echo "[1/2] 检查 Podman machine..."
if podman machine list --format "{{.Running}}" | grep -q "true"; then
    echo "✅ Podman machine 运行中"
else
    echo "⏸️  Podman machine 未运行，正在启动..."
    podman machine start
    if [ $? -eq 0 ]; then
        echo "✅ Podman machine 启动成功"
        sleep 2
    else
        echo "❌ Podman machine 启动失败"
        exit 1
    fi
fi

# 检查 MySQL 容器
echo ""
echo "[2/2] 检查 MySQL 容器..."

if podman ps --format "{{.Names}}" | grep -q "^$CONTAINER_NAME$"; then
    echo "✅ MySQL 容器已在运行中"
    CONTAINER_STATUS="running"
else
    echo "⏸️  MySQL 容器已停止，正在启动..."
    if podman start $CONTAINER_NAME; then
        echo "✅ MySQL 容器启动成功"
        CONTAINER_STATUS="started"
    else
        echo "❌ MySQL 容器启动失败"
        exit 1
    fi
fi

# 显示详细信息
echo ""
echo "========== 当前状态 =========="
echo "Podman machine:"
podman machine list
echo ""
echo "MySQL 容器:"
podman ps --filter name=$CONTAINER_NAME --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

if [ "$CONTAINER_STATUS" = "started" ]; then
    echo ""
    echo "📝 容器刚刚启动，查看最新日志："
    podman logs --tail 5 $CONTAINER_NAME
fi