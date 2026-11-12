package api.tools;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

public class WechatUtil {
    //	配置自己的app_id、app_secret
    private static final String APP_ID = "wx1009da0e793017d8";
    private static final String APP_SECRET = "7387c8a9c80f7ef1e1f78416dcce879e";

    public static String getOpenId(String loginCode) {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        String requestUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("appid", APP_ID)
                .queryParam("secret", APP_SECRET)
                .queryParam("js_code", loginCode)
                .queryParam("grant_type", "authorization_code")
                .toUriString();

        HttpResponse response = HttpUtil.createGet(requestUrl).execute();

        // 获取 session_key 和 openid
        JSONObject parseObj = JSONUtil.parseObj(response.body());

        String openid = (String) parseObj.get("openid");
        return openid;
    }

    public static String getAccessToken(){
        String url = "https://api.weixin.qq.com/cgi-bin/token";
        String requestUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("appid", APP_ID)
                .queryParam("secret", APP_SECRET)
                .queryParam("grant_type", "client_credential")
                .toUriString();

        HttpResponse response = HttpUtil.createGet(requestUrl).execute();

        // 获取 session_key 和 openid
        JSONObject parseObj = JSONUtil.parseObj(response.body());

        String access_token = parseObj.get("access_token").toString();
        return access_token;
    }

    //获取用户手机号
    public static String getPhoneNumber(String access_token,String code) {
        String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber";
        String requestUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("access_token", access_token)
                .toUriString();

        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("code",code);

        System.out.println(access_token +" = " +code);
        System.out.println("requestUrl = " + requestUrl);


        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(requestUrl);
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
        httpPost.setHeader("Accept", "application/json");
        try {
            httpPost.setEntity(new StringEntity(JSON.toJSONString(paramMap)));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == 200) {
                HttpEntity responseEntity = response.getEntity();
                JSONObject parseObj = JSONUtil.parseObj(EntityUtils.toString(responseEntity));
                String phone_info = parseObj.get("phone_info").toString();
                System.out.println(" phone_info = " + phone_info);
                if (StringUtils.isNotEmpty(phone_info)) {
                    JSONObject jsonObject = JSONUtil.parseObj(phone_info);
                    return jsonObject.get("purePhoneNumber").toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}