#!/bin/bash

MYSQL_CONTAINER="mysql-container"
NACOS_HOME="/Users/chase/mycode/springalibaba/nacos"

echo "========== 启动服务 =========="

# 第一步：检查 Podman machine
echo "[1/4] 检查 Podman machine..."
if ! podman machine list --format "{{.Running}}" | grep -q "true"; then
    echo "⏸️  Podman machine 未运行，正在启动..."
    podman machine start || { echo "❌ Podman machine 启动失败"; exit 1; }
    sleep 2
else
    echo "✅ Podman machine 运行中"
fi

# 第二步：检查 MySQL 容器
echo ""
echo "[2/4] 检查 MySQL 容器..."

if podman ps --format "{{.Names}}" | grep -q "^$MYSQL_CONTAINER$"; then
    echo "✅ MySQL 容器已在运行中"
else
    if podman ps -a --format "{{.Names}}" | grep -q "^$MYSQL_CONTAINER$"; then
        echo "⏸️  MySQL 容器存在但未运行，正在启动..."
        podman start $MYSQL_CONTAINER || { echo "❌ MySQL 启动失败"; exit 1; }
        echo "✅ MySQL 启动成功"

        # 等待 MySQL 完全就绪
        echo "等待 MySQL 初始化..."
        sleep 10
    else
        echo "❌ MySQL 容器不存在！"
        exit 1
    fi
fi

# 第三步：检查并启动本地 Nacos
echo ""
echo "[3/4] 检查本地 Nacos..."

# 检查 8848 端口是否被占用
if lsof -i:8848 > /dev/null 2>&1; then
    NACOS_PID=$(lsof -i:8848 -t 2>/dev/null)
    echo "✅ Nacos 已在运行中 (PID: $NACOS_PID, 端口: 8848)"
else
    echo "⏸️  Nacos 未运行，正在启动..."

    # 进入 Nacos 目录并启动
    cd "$NACOS_HOME/bin" || { echo "❌ 无法进入 Nacos 目录: $NACOS_HOME/bin"; exit 1; }

    # 启动 Nacos
    ./startup.sh -m standalone

    if [ $? -eq 0 ]; then
        echo "✅ Nacos 启动命令已执行"
        echo "等待 Nacos 启动..."
        sleep 5

        # 再次检查端口
        if lsof -i:8848 > /dev/null 2>&1; then
            echo "✅ Nacos 启动成功 (端口 8848 已监听)"
        else
            echo "⚠️  Nacos 启动中，请稍后访问控制台确认"
        fi
    else
        echo "❌ Nacos 启动失败"
        exit 1
    fi
fi

# 第四步：显示最终状态
echo ""
echo "[4/4] 最终状态："
echo ""
echo "MySQL 容器："
podman ps --filter name=$MYSQL_CONTAINER --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
echo ""
echo "Nacos 端口状态："
if lsof -i:8848 > /dev/null 2>&1; then
    echo "✅ 端口 8848 已被监听"
    lsof -i:8848 | grep -v "command" | head -2
else
    echo "❌ 端口 8848 未被监听"
fi

# 显示访问信息
echo ""
echo "========== 访问信息 =========="
echo "MySQL: localhost:3306"
echo "Nacos 控制台: http://localhost:8080/"
echo "Nacos 默认账号: nacos/nacos"
echo ""
echo "Nacos 日志查看: tail -f $NACOS_HOME/logs/start.out"