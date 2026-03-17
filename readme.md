# PowerCloud 微服务项目

Spring Cloud Alibaba 微服务项目，包含用户服务和订单服务。

## 项目架构

```
powercloud (父项目)
├── user-service (用户服务)
│   ├── user-service-api (API 模块)
│   └── user-service-core (核心服务模块)
└── order-service (订单服务)
```

## 技术栈

- Spring Boot 3.2.5
- Spring Cloud 2023.0.0
- Spring Cloud Alibaba 2025.0.0.0
- Nacos (服务注册与配置中心)
- MySQL (数据库)
- OpenFeign (服务调用)

---

## 启动步骤

### 前置条件

1. **Podman** (用于运行 MySQL 容器)
2. **Maven** (项目构建)
3. **JDK 17+**
4. **Nacos** (需提前下载并配置)

### 第一步：启动基础服务

#### 1.1 启动 MySQL

```bash
# 方式一：使用提供的脚本启动 MySQL
./start-mysql.sh

# 方式二：手动启动
podman machine start                    # 启动 Podman machine
podman start mysql-container            # 启动 MySQL 容器
```

#### 1.2 启动 Nacos

```bash
# 方式一：使用提供的脚本启动（推荐）
./start-nacos.sh

# 方式二：手动启动
cd /Users/chase/mycode/springalibaba/nacos/bin
./startup.sh -m standalone
```

**Nacos 控制台**: http://localhost:8848/nacos
**默认账号**: nacos / nacos

> 注意：首次使用 Nacos 需在控制台创建命名空间 `dev-namespace-id`

---

### 第二步：配置 Nacos

1. 登录 Nacos 控制台 (http://localhost:8848/nacos)
2. 创建命名空间：
   - 命名空间 ID: `dev-namespace-id`
   - 命名空间名称: `dev`
3. 在 `user` 分组下创建配置文件：
   - `user-service.yaml` (用户服务配置)
   - `order-service.yaml` (订单服务配置)

---

### 第三步：启动微服务

#### 3.1 编译项目

```bash
# 在项目根目录执行
mvn clean package -DskipTests
```

#### 3.2 启动用户服务

```bash
# 方式一：IDE 中运行
# 运行主类: com.chase.userservice.UserServiceApplication

# 方式二：命令行运行
cd user-service/user-service-core
mvn spring-boot:run

# 或运行编译后的 JAR
java -jar user-service-core/target/user-service-core-1.0-SNAPSHOT.jar
```

**用户服务端口**: 8081 (默认)

#### 3.3 启动订单服务

```bash
# 方式一：IDE 中运行
# 运行主类: com.chase.orderservice.OrderServiceApplication

# 方式二：命令行运行
cd order-service
mvn spring-boot:run

# 或运行编译后的 JAR
java -jar order-service/target/order-service-1.0-SNAPSHOT.jar
```

**订单服务端口**: 8082 (默认)

---

## 接口测试

### 用户服务

| 接口 | 方法 | URL | 说明 |
|------|------|-----|------|
| 获取用户信息 | GET | `http://localhost:8081/user/{id}` | 根据ID获取用户信息 |

### 订单服务

| 接口 | 方法 | URL | 说明 |
|------|------|-----|------|
| 创建订单 | GET | `http://localhost:8082/orders/create?userId=1&productName=iPhone+16&price=9999&quantity=1` | 创建订单接口 |

**测试示例**:
```bash
# 创建订单
curl "http://localhost:8082/orders/create?userId=1&productName=iPhone+16&price=9999&quantity=1"

# 获取用户信息
curl "http://localhost:8081/user/1"
```

---

## 服务注册信息

启动后可在 Nacos 控制台「服务管理」→「服务列表」中查看：

- `user-service` - 用户服务
- `order-service` - 订单服务

---

## 常见问题

### 1. Nacos 启动失败

检查端口是否被占用：
```bash
lsof -i:8848
```

### 2. 服务无法注册到 Nacos

1. 确认 Nacos 已启动
2. 检查命名空间 `dev-namespace-id` 是否创建
3. 检查配置文件中的用户名密码是否正确

### 3. MySQL 连接失败

```bash
# 检查 MySQL 容器状态
podman ps -a | grep mysql-container

# 查看 MySQL 日志
podman logs mysql-container
```
