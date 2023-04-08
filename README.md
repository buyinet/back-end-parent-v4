# Kantboot开发平台

## 适用项目
Kantboot开发平台，可以应用在多种小程序、App项目的开发中, 
也可以用于快速搭建后台管理系统，提高开发效率。

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
#### 1.1 常用工具模块(util-common)
项目中默认使用的密码加密操作是util-common模块中的KantbootPassword类
>具体操作请查看<a href="/util/util-common/README.md">util-common模块的README.md</a>
#### 1.2 核心工具模块(util-core)
项目中使用@RequestParam注解之所以可以解析json参数，是因为util-core模块中的resolver包中的配置器的作用
>具体操作请查看<a href="/util/util-core/README.md">util-core模块的README.md</a>