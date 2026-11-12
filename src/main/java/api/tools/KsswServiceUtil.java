package api.tools;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class KsswServiceUtil {
    //	昆山水务平台分配的ID
    private static final String appId = "c31e3f5d6e9f4c10b55716c71ec96d62";

    public static String getVerifyToken(String token)  {
        String requestUrl = "https://www.kssfb.com:6969/prod-api/services/verifyToken?appId="+appId;
        if (token == null || StringUtils.isEmpty(token)) {
            System.out.println("token 不能为空");
            return null;
        }
//        //定义提交类型
//        String contentType = "application/json";
//
//        OkHttpClient client = new OkHttpClient();
//        //传参调用方法
//        Request request = new Request.Builder()
//                .url(requestUrl)
//                .addHeader("Authorization", "Bearer "+token)
//                .build();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Bearer "+token);

        String content = OkhttpsUtils.doGet(requestUrl,null,headers);
        // 获取 session_key 和 openid
        JSONObject parseObj = JSONUtil.parseObj(content);
        if(Integer.parseInt(parseObj.get("code").toString()) == 200){
            return parseObj.get("data").toString();
        }
        return "";
    }
}
