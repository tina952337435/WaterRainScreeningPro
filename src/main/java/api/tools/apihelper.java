package api.tools;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;


import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

/**
 * 这里键入类的描述
 *
 * @author 高起
 * @版权： 版权所有 (c) 2018
 * @see：
 * @创建日期： 2018/12/10 14:49
 * @功能说明：
 */
public class apihelper {
    /**
     * 发送post 请求访问本地应用并根据传递参数的不同返回不同的结果
     * @param url 访问地址
     * @param parmasMap 传递的参数
     */
    public static String apipost(String url, String parmasMap, HashMap<String, Object> header){
        String data = null;
        //创建默认的httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        if (header != null){
            header.forEach((key,value)->{
                httpPost.addHeader(key,value.toString());
            });
        }else {
            header.put("Content-Type","application/json;charset=UTF-8");
        }
        try{
            // 设置请求体（参数）
            StringEntity entity = new StringEntity(parmasMap, "UTF-8");
            httpPost.setEntity(entity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try{
                HttpEntity entityResult = response.getEntity();
                if (entityResult != null){
                    data = EntityUtils.toString(entityResult);
                    return data;
                }
                return data;
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                response.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                httpClient.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return data;
    }
    public static String apiget(String url, String parmasMap, HashMap<String, Object> header){
        String data = null;
        //创建默认的httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        if (header != null){
            header.forEach((key,value)->{
                httpGet.addHeader(key,value.toString());
            });
        }
        try{
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try{
                HttpEntity entityResult = response.getEntity();
                if (entityResult != null){
                    data = EntityUtils.toString(entityResult);
                    return data;
                }
                return data;
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                response.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                httpClient.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return data;
    }


    public static String apiUpload(String uploadUrl,String filePath)  {
        String result="";
        int fileId = 0;
        try {
            OkHttpClient client = new OkHttpClient();

            // 读取文件为二进制流
            byte[] fileData = java.nio.file.Files.readAllBytes(new File(filePath).toPath());

            // 构建 JSON 请求体
            String jsonPayload = buildJsonPayload(fileData, fileId);

            // 构建请求
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonPayload);
            Request request = new Request.Builder()
                    .url(uploadUrl)
                    .post(body)
                    .build();

            // 发送请求并处理响应
            try (Response response = client.newCall(request).execute()) {
                result=response.body().string();

                System.out.println("Response: " + response);
                System.out.println("Status Code: " + response.code());
                System.out.println("Response Body: " + response.body());

            }
        }catch (IOException ex) {
            System.out.println("apiUpload: " + ex.getMessage());
        }
        return result;
    }
    private static String buildJsonPayload(byte[] fileData, int fileId) {
        String base64File = Base64.getEncoder().encodeToString(fileData);
        System.out.println("base64File: " + base64File);
        return String.format("{\"file\":\"%s\",\"file_id\":%d}", base64File, fileId);
    }

    public static String apiUploadJava(String uploadUrl,String filePath){
        String result="";
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file",filePath,
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File(filePath)))
                .build();
        Request request = new Request.Builder()
                .url(uploadUrl)
                .method("POST", body)
//                .addHeader("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpbk5hbWUiOiJHYW9xaSIsInBlcm1pc3Npb24iOiI4ZTk0NDAxYjMyN2MwMTk1OGI1OWU3OGQ3YmY4NTNiOCIsImV4cCI6MTc0MTk1MzE2Nn0.rAFlFLdE3IbqxdkfVXGE0BdV1AqLU_l7IUy9WIJF_q4")
//                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .build();
        try {
            Response response = client.newCall(request).execute();
            result=response.body().string();
//            System.out.println("Response: " + response);
//            System.out.println("Status Code: " + response.code());
//            System.out.println("Response Body: " + response.body());
        }catch (IOException ex) {
        System.out.println("apiUpload: " + ex.getMessage());
    }
        return result;
    }
}
