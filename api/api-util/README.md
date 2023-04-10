# Api工具

## 百度翻译
### 1. 通用文本翻译
```java
BaiduTranslateParam baiduTranslateParam = new BaiduTranslateParam();
baiduTranslateParam.setAppid("百度翻译开放平台的appid");
baiduTranslateParam.setSecret("百度翻译开放平台的密钥");

// 翻译的文本
baiduTranslateParam.setQ("你好");
// 翻译的源语言，可以使用百度翻译开放平台的语言列表中的编码，后期会扩展也可以使用国际化的语言编码
baiduTranslateParam.setFrom("zh");
// 翻译的目标语言，可以使用百度翻译开放平台的语言列表中的编码，后期会扩展也可以使用国际化的语言编码
baiduTranslateParam.setTo("en");

// 获取翻译结果
BaiduTranslateResult translate = BaiduTranslateUtil.translate(baiduTranslateParam);
// 输出结果：{"src":"你好","dst":"Hello"}
System.out.println(JSON.toJSONString(translate));
```
> 顺带一提，生成国际化的字典的service就是通过这个工具实现的

#### 生成国际化字典的api
此api时在此顺带一提，
使用此接口可以快速翻译几十种语言倒国际化字典

URL:
```shell
{{url}}/api/baiduTranslate/generateDictI18n
```

参数说明：
```json
{
    "q":"权限不足",
    "from":"zh",
    "dictGroupCode":"stateError",
    "dictCode":"insufficientPermissions"
}
```

