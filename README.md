# Kantboot开发架构

## 适用项目
Kantboot开发架构，是一个便捷开发、便捷多语言的开发架构，
可以应用在多种小程序、App项目的开发中, 也可以用于快速搭建后台管理系统，提高开发效率。

## 平台介绍
### 开发环境
- 语言：JAVA17+
- 框架：SpringBoot3.0.5
- IDE(后端)：IntelliJ IDEA
- IDE(前端)：WebStorm、VSCode、HbuilderX
- 依赖管理：Maven
- 数据库：任意Jpa支持的数据库
- 缓存：Redis

### 后端
- 基础架构：SpringBoot3.0.5
- 持久层：Spring Data JPA
- 安全框架：Kantboot System Security
- 数据库连接池：Alibaba Druid
- 日志打印：log4j2
- i18n国际化：Kantboot System I18n
- 代码生成：Kantboot System Code Generator

### 前端(PC端)
- 基础框架：Vue3.0
- 路由管理：Vue Router
- 状态管理：Pinia
- UI框架：Element Plus
- 代码生成：Kantboot System Code Generator
- 代码规范：ESLint
- 其它：Axios、TypeScript、Sass、ECharts等

### 前端(移动端、小程序端)
- 基础框架：UniApp
- UI框架：uView、Kantboot UI
- 代码生成：Kantboot System Code Generator

### 支持的数据库
- MySQL
- Oracle
- SQL Server
- PostgreSQL
- H2
- HSQL
- SQLite
- DB2
- 达梦、人大金仓

## 项目模块
### 1. 工具模块(util)
#### 1.1 常用工具模块(util-common) - 无入侵性
项目中默认使用的密码加密操作是util-common模块中的
<a href="/util/util-common/src/main/java/com/kantboot/util/common/password/KantbootPassword.java">KantbootPassword</a>类
>具体操作请查看<a href="/util/util-common/README.md">util-common模块的README.md</a>
#### 1.2 核心工具模块(util-core) - 有入侵性
项目中使用@RequestParam注解之所以可以解析json参数，是因为util-core模块中的resolver包中的配置器的作用

该模块有一定的入侵性，但是可以大大提高开发效率，如果是在非Kantboot开发架构中使用，可以不使用该模块

>具体操作请查看<a href="/util/util-core/README.md">util-core模块的README.md</a>
### 2. 系统模块(system)
#### 2.1 系统模型模块(system-model)
系统模型模块，是系统中最基础的实体类，如用户、角色、菜单、权限、设置等
#### 2.2 系统服务模块(system-service)
系统服务模块，是系统中最基础的服务类

其中
<a href="/system/system-service/src/main/java/com/kantboot/system/service/ISysDictI18nService.java">ISysDictI18nService</a>
接口是系统中的国际化服务类，便于系统中的国际化功能
