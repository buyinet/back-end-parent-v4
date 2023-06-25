package com.kantboot.api.util.uni.push;

import com.alibaba.fastjson2.JSONObject;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * 发送推送的工具类
 *
 * @author 方某方
 */
public class PushUtil {

    @SneakyThrows
    public static JSONObject sendPush(String url, Map<String, Object> map) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .post(okhttp3.RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSONObject.toJSONString(map)))
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        return JSONObject.parseObject(responseBody);
    }


//    public static void main(String[] args) {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("cid", "fcaab39df1186084c414cd241be2a1e4");
//        map.put("forceNotification", false);
//        map.put("title", "123");
//        map.put("content", "123");
//        map.put("payload", new HashMap<String, Object>() {{
//            put("a", "1");
//        }});
//        sendPush(map);
//    }

}