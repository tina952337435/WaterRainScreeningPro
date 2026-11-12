package api.tools;

//import api.data.ksdata.SYS_KNOWLEDGEBASEData;
//import api.pojo.ksdata.SYS_KNOWLEDGEBASEPojo;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SpeechUtils {
    public static final String API_KEY = "cKoyacq4aPmQwg2lwd0eaVCK";
    public static final String SECRET_KEY = "hMkZArsCKbwXfLp9RaPgvhsMcELwBKWS";
    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    /**
     * 根据语音识别为文字
     * */
    public String getSpeechResult(byte[] b,String strToken) throws IOException, JSONException{
        String format = "wav";//语音文件的格式，pcm/wav/amr/m4a。不区分大小写。推荐pcm文件
        int rate = 16000;//采样率,16000,8000
        int channel = 1;//声道数
        String cuid = "keuCLY8ogMJSeqatWRBI4PYdZU0aTs3l";//用户唯一标识

        String token = null != strToken ? strToken : createAccessToken();
        Map<String, String> fileContentAsBase64 = getFileContentAsBase64(b, false);
        // speech 可以通过 getFileContentAsBase64("")方法获取,如果Content-Type是application/x-www-form-urlencoded时,第二个参数传true
        String speech = fileContentAsBase64.get("speech");//语音文件Base编码
        String len = fileContentAsBase64.get("len");//本地语音文件的的字节数
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "{\"format\":\""+ format +"\",\"rate\":"+ rate +",\"channel\":"+ channel +",\"cuid\":\""+ cuid +"\",\"token\":\""+ token +"\",\"speech\":\""+ speech +"\",\"len\":"+ len +"}");
        Request request = new Request.Builder()
                .url("https://vop.baidu.com/server_api")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return response.body().string();
    }

    /*
    * 文字拆解为 （关键字、关键词）
    * */
    public Map getSpeechWordResult(String text,String strToken) throws IOException, JSONException {
        String token = null != strToken ? strToken : createAccessToken();
//        String token = "24.17375ac0d9f302d2c132f4876362d9dd.2592000.1690962596.282335-35643877";
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"text\":\"" + text + "\"}");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/nlp/v1/lexer?charset=UTF-8&access_token=" + token)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        String obj =  response.body().string();
//        System.out.println(obj);
        Map map = new ObjectMapper().readValue(obj, Map.class);
        return map;
    }

    /**
     * 获取token信息
     * */
    public static String createAccessToken() throws IOException, JSONException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token?client_id="+ API_KEY +"&client_secret="+ SECRET_KEY +"&grant_type=client_credentials")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();

        String res = response.body().string();
        System.out.println(res);
        try {
            Map<String,Object> map = JsonToMapUtils.convertJsonToMap(res);
            if(null != map){
                return JsonToMapUtils.GetValueByKey(map,"access_token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取文件base64编码
     *
     * @param b 字节数组
     * @param urlEncode 如果Content-Type是application/x-www-form-urlencoded时,传true
     * @return base64编码信息，不带文件头
     * @throws IOException IO异常
     */
    static Map <String,String> getFileContentAsBase64(byte[] b, boolean urlEncode) throws IOException {
        //byte[] b = Files.readAllBytes(Paths.get(path));
        String base64 = Base64.getEncoder().encodeToString(b);
        if (urlEncode) {
            base64 = URLEncoder.encode(base64, "utf-8");
        }
        Map <String,String> map = new HashMap<>();
        map.put("speech",base64);
        map.put("len", String.valueOf(b.length));
        return map;
    }

    @Autowired
//    private static SYS_KNOWLEDGEBASEData sysKnowledgebaseData;

//    public static List<SYS_KNOWLEDGEBASEPojo> getKey() {
//        List<SYS_KNOWLEDGEBASEPojo> mList = sysKnowledgebaseData.selectList("(.*?过程.*?)|(.*?太湖.*?)","");
//        return  mList;
//    }
    public static void main(String []args) throws IOException{

//        Sample ut = new Sample();
////        try {
////            System.out.println(" token = " + ut.createAccessToken());
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
//        List<SYS_KNOWLEDGEBASEPojo> mList = getKey();

//        System.out.println(mList);
    }

}
