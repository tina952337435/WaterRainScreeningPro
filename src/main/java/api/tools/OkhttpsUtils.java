package api.tools;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: liukai
 * @date: 2025/2/18 下午5:39
 */
@Slf4j
public class OkhttpsUtils {
    /**
     * 获取客户端链接
     */
    private static OkHttpClient getClient() {
        OkHttpClient okHttpClient = OKHttpClientBuilder.buildOKHttpClient().
                connectTimeout(60000, TimeUnit.SECONDS).
                readTimeout(60000, TimeUnit.SECONDS).
                writeTimeout(60000, TimeUnit.SECONDS).
                build();
        return okHttpClient;
    }

    private static Request getRequest(String url, Map<String,String> header) throws MalformedURLException {
        Request.Builder builder = getBuilder(header);
        return builder.url(url).build();
    }

    private static Request.Builder getBuilder(Map<String,String> header) {
        Request.Builder builder = new Request.Builder();

        builder.addHeader("accept", "application/json").
                addHeader("connection", "Keep-Alive").
                addHeader("Content-Type", "application/json;charset=UTF-8");
        if(header != null && header.entrySet().size()>0){
            for(Map.Entry<String,String>  entry:header.entrySet()){
                builder.addHeader(entry.getKey(),entry.getValue());
            }
        }
        return builder;
    }

    /**
     * doGet请求
     */
    public static String doGet(String url, String param, Map<String,String> header) {
        if (param != null) {
            url = url + "?" + param;
        }

        String result = null;
        try {
            OkHttpClient okHttpClient = getClient();
            Request request = getRequest(url,header);
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    result = body.string();
                } else {
                    throw new IOException("response body is null");
                }
            } else {
                response.close();
            }
        } catch (IOException e) {
            log.error("GET请求异常,url = {}", url, e);
            throw new RuntimeException("GET请求异常,url = "+ url+";"+e.getMessage());
        }
        return result;
    }

    public static Response doGetResponst(String url, String param, Map<String,String> header) {
        if (param != null) {
            url = url + "?" + param;
        }

        Response result = null;
        try {
            OkHttpClient okHttpClient = getClient();
            Request request = getRequest(url,header);
            Response response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            log.error("GET请求异常,url = {}", url, e);
            throw new RuntimeException("GET请求异常,url = "+ url+";"+e.getMessage());
        }
        return result;
    }

    /**
     * doPost请求
     */
    public static String doPost(String url, String param,Map<String,String> header) {

        OkHttpClient okHttpClient = getClient();
        Request.Builder builder = getBuilder(header);
        String result = null;
        try {
            MediaType mediaType = MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE);
            if (header != null && !StringUtils.isEmpty(header.get("Content-Type"))){
                mediaType = MediaType.parse(header.get("Content-Type") );
            }
            RequestBody requestBody = RequestBody.create(mediaType,
                    param.getBytes("UTF-8"));
            builder.post(requestBody);
            Request request = builder.url(url).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    result = body.string();
                } else {
                    throw new IOException("response body is null");
                }
            } else {
                response.close();
            }
        } catch (Exception e) {
            log.error("POST请求异常,url = {}", url, e);
            throw new RuntimeException("POST请求异常,url = "+ url+";"+e.getMessage());
        }
        return result;
    }

    /**
     * doDelete请求
     */
    public static String doDelete(String url, String param,Map<String,String> header) {

        OkHttpClient okHttpClient = getClient();
        Request.Builder builder = getBuilder(header);
        String result = null;
        try {
            if (param != null) {
                RequestBody requestBody = RequestBody.create(MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE),
                        param.getBytes("UTF-8"));
                builder.delete(requestBody);
            } else {
                builder.delete();
            }
            Request request = builder.url(url).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    result = body.string();
                } else {
                    throw new IOException("response body is null");
                }
            } else {
                response.close();
            }
        } catch (Exception e) {
            log.error("DELETE请求异常,url = {}", url, e);
        }
        return result;
    }

    /**
     * doPut请求
     */
    public static String doPut(String url, String param,Map<String,String> header) {
        OkHttpClient okHttpClient = getClient();
        Request.Builder builder = getBuilder(header);
        String result = null;
        try {
            RequestBody requestBody = RequestBody.create(MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE),
                    param.getBytes("UTF-8"));
            builder.put(requestBody);
            Request request = builder.url(url).build();
            Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    result = body.string();
                } else {
                    throw new IOException("response body is null");
                }
            } else {
                response.close();
            }
        } catch (Exception e) {
            log.error("PUT请求异常,url = {}", url, e);
        }
        return result;
    }

    /**
     * doPost请求
     */
    public static Response doPostResponse(String url, String param, Map<String,String> header) {
        Response response = null;
        OkHttpClient okHttpClient = getClient();
        Request.Builder builder = getBuilder(header);
        try {
            RequestBody requestBody = RequestBody.create(MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE),
                    param.getBytes("UTF-8"));
            builder.post(requestBody);
            Request request = builder.url(url).build();
            response = okHttpClient.newCall(request).execute();
        } catch (Exception e) {
            log.error("POST请求异常,url = {}", url, e);
            throw new RuntimeException("POST请求异常,url = "+url + "异常信息："+e.getMessage());
        }
        return response;
    }



    public static String postFormData(String url,Map params, Map headers){
        String result = null;
        // 创建OkHttpClient 自定义实例，解决https ssl 证书问题
//        OkHttpClient client = new OkHttpClient();
        OkHttpClient client = getClient();

        // 创建FormBody对象，添加表单字段
        FormBody.Builder formBuilder = new FormBody.Builder();
        // 将传进来包含的参数键值对的map集合遍历出来
        for(Object o : params.keySet()){
            formBuilder.add(o.toString(),String.valueOf(params.get(o.toString())));
        }
        // 将frombody初始化到request对象中然后拿去请求
        RequestBody formBody = formBuilder.build();


//        // 创建Request对象，指定URL和请求体（表单数据）
//        Request request = new Request.Builder()
//                .url(url) // 替换为你的目标URL
//                .post(formBody) // 使用POST方法发送表单数据
//                .build();
        Request.Builder reqBuilder = new Request.Builder();
        if (headers!=null){
            // 设置请求头
            for (Object o : headers.keySet()) {
                reqBuilder.addHeader(o.toString(), (String) headers.get(o.toString()));
            }
        }

        Request request = reqBuilder.url(url).post(formBody).build();


        try {
            // 发送请求并获取响应
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    result = body.string();
                    // 打印响应体内容
                    System.out.println(result);
                } else {
                    throw new IOException("response body is null");
                }
            } else {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace(); // 处理异常，例如网络问题或服务器错误等
        }

        return result;
    }
}
