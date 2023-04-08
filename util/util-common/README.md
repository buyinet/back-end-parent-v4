# 常用工具类
> 用于基础的工具类、如文件操作、字符串操作、日期操作等无耦合性的工具类

## 1. 文件操作
### 1.1 获取文件的后缀名
```
String suffix = FileUtil.getSuffix("/file/test.txt");
// 返回结果：/file/test.txt = txt
System.out.println("/file/test.txt = " + suffix);

suffix = FileUtil.getSuffix("/file/test"); 
// 返回结果：/file/test = null
System.out.println("/file/test = " + suffix);

suffix = FileUtil.getSuffix("test.png");
// 返回结果：test.png = png
System.out.println("test.png = " + suffix);
```

### 1.2 获取文件的ContentType
```
String contentType = FileUtil.getContentType("/file/test.txt");

// 返回结果：/file/test.txt = text/plain
System.out.println("/file/test.txt = " + contentType);

contentType = FileUtil.getContentType("/file/test");

// 返回结果：/file/test = null
System.out.println("/file/test = " + contentType);

contentType = FileUtil.getContentType("test.png");

// 返回结果：test.png = image/png
System.out.println("test.png = " + contentType);
```

1.3 从网络Url中下载文件
```
String url = "https://www.baidu.com/img/bdlogo.gif";

// 下载到D盘的test目录下，文件名为baidu.png，重复下载会覆盖
FileUtil.downloadFromUrl(url,"D:\\test","baidu.png");
```

1.4 获取文件的字节流
```
// 获取D盘的test目录下的baidu.png文件的字节流
FileUtil.getByte("D:\\test","baidu.png");
```

1.5 获取文件的MD5值
```
// 获取D盘的test目录下的baidu.png文件的MD5值
FileUtil.getMD5("D:\\test","baidu.png");
```

## 2. (KantbootPassword)密码操作
### 2.1 加密
```
KantbootPassword kantbootPassword = new KantbootPassword();

// 每次加密的结果都不一样
// 加密后的密码：kantbootPassword.encode = 2e674d9859894505bb671edabebfdad3.6f287f1dc370d296d63d8f2be67e311f
System.out.println("kantbootPassword.encode = " + kantbootPassword.encode("123456"));
```

### 2.2 比较密码是否正确
```
KantbootPassword kantbootPassword = new KantbootPassword();

// 比较成功，返回结果：密码比较 = true
System.out.println("密码比较 = " + kantbootPassword.matches("123456",
                "2e674d9859894505bb671edabebfdad3.6f287f1dc370d296d63d8f2be67e311f"));

// 比较失败，返回结果：密码比较 = false
System.out.println("密码比较 = " + kantbootPassword.matches("123456",
                "2e674d9859894505bb671edabebfdad3.6f287f1dc370d296d63d8f2be67e3112"));
```