# 核心工具模块

## 导入此包后，可以使用以下功能
### 1. 使用雪花算法
```java
@Id
// start:重点两行代码
@GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
@GeneratedValue(generator = "snowflakeId")
// end:重点两行代码
@Column(name = "id")
private Long id;
```
### 2. 使用@RequestParam注解解析json参数
导入此包后，可以使用@RequestParam注解解析json参数(请求头为application/json)
### 3. 会使用FastJson2来序列化和反序列化
### 4. 可以使用RedisUtil工具类操作Redis
> 详情请求请查看<a href="/util/util-core/src/main/java/com/kantboot/util/core/redis/RedisUtil.java">RedisUtil.java</a>