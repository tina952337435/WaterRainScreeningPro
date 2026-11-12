package api.tools;

import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.gson.JsonArray;

//@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
@Configuration
public class HikHelp {
//    private final Logger logger = LoggerFactory.getLogger(HikHelp.class);

    static {
        ArtemisConfig.host = "192.168.200.66:50443"; // artemis网关服务器ip端口
        ArtemisConfig.appKey = "29160934";  // 秘钥appkey
        ArtemisConfig.appSecret = "7aKG9YAFIwgaHzgWht50";// 秘钥appSecret

        //苏州测试
//        ArtemisConfig.host = "172.16.63.61:443"; // artemis网关服务器ip端口
//        ArtemisConfig.appKey = "25358335";  // 秘钥appkey
//        ArtemisConfig.appSecret = "4LxHA0ccd9S5QroeSdMp";// 秘钥appSecret
    }

    /**
     *设置OpenAPI接口的上下文
     */
    private static final String ARTEMIS_PATH = "/artemis";


    /*
     * @Description: 分页获取区域信息
     * @author: Orange
     * @Param: /api/resource/v1/regions/root
     * @Return: java.lang.String
     * @Date: 2020/11/17 11:21
     */
    public static String getRegionsPageList(int pageNo, int pageSize){

        //获取区域信息路径  /api/resource/v1/regions/root
        final String getRegionsList = ARTEMIS_PATH + "/api/resource/v1/regions";

        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getRegionsList);
            }
        };
        //定义提交类型
        String contentType = "application/json";
        //参数装换
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("pageNo", pageNo);
        jsonBody.put("pageSize", pageSize);
        String body = jsonBody.toString();
        //传参调用方法
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);

        //返回编码信息
        return result;
    }

    /*
     * @Description: 根据区域编号获取下级监控点列表
     * @author: Orange
     * @Param:
     * @Return: java.lang.String
     * @Date: 2020/11/17 11:36
     */
    public static String getCamerasByRegionCode(String regionIndexCode, int pageNo, int pageSize) {

        //根据区域编号获取下级监控点列表路径
        final String getRootApi = ARTEMIS_PATH + "/api/resource/v1/regions/regionIndexCode/cameras";

        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getRootApi);
            }
        };

        String contentType = "application/json";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("pageNo", pageNo);
        jsonBody.put("pageSize", pageSize);
        jsonBody.put("regionIndexCode", regionIndexCode);
        String body = jsonBody.toString();

        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);

        //返回编码信息
        return result;
    }

    /*
     * @Description: 根据摄像头编码code获得播放流
     * @author: Orange
     * @Param:
     * @Return: java.lang.String
     * @Date: 2020/11/17 13:19
     */
    public static String getCamerasUrlByCode(String cameraIndexCode,String protocol) {
        if(CommonUtills.isEmpty(protocol)){
            protocol="ws";
        }

        final String getCamsApi = ARTEMIS_PATH + "/api/video/v1/cameras/previewURLs";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getCamsApi);
            }
        };

        String contentType = "application/json";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("cameraIndexCode", cameraIndexCode);//监控点唯一标识
        jsonBody.put("streamType", 0);//码流类型,0:主码流，1:子码流，2:第三码流，参数不填，默认为主码流
        jsonBody.put("protocol", protocol);//取流协议,“rtsp”:RTSP协议,“rtmp”:RTMP协议,“hls”:HLS协议
        jsonBody.put("transmode", 1);//传输协议,0:UDP，1:TCP，默认是TCP
        jsonBody.put("expand", "streamform=ps");//扩展内容,格式：key=value
        String body = jsonBody.toString();
        System.out.println(body);
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);
        System.out.println(result);

        //返回编码信息
        return result;
    }

    /*
     * @Description: 获取摄像头列表
     * @author: Orange
     * @Param: int
     * @Return: java.lang.String
     * @Date: 2020/11/17 10:32
     */
    public static String getAllRegionInfo(int pageNo, int pageSize) {

        //获取摄像头列表路径
        final String getCameraList = ARTEMIS_PATH + "/api/resource/v1/cameras";

        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getCameraList);
            }
        };
        //定义提交类型
        String contentType = "application/json";
        //参数装换
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("pageNo", pageNo);
        jsonBody.put("pageSize", pageSize);
        String body = jsonBody.toString();
        //传参调用方法
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);
        //返回结果
        return result;
    }


    /*
     * @Description: 获取监控点在线状态
     * @author: Orange
     * @Param: int
     * @Return: java.lang.String
     * @Date: 2020/11/17 10:32
     */
    public static String getAllOnlineInfo(int pageNo, int pageSize, List<String> indexCodes) {

        //获取摄像头列表路径
        final String getCameraList = ARTEMIS_PATH + "/api/nms/v1/online/camera/get";

        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getCameraList);
            }
        };
        //定义提交类型
        String contentType = "application/json";
        //参数装换
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("pageNo", pageNo);
        jsonBody.put("pageSize", pageSize);
        jsonBody.put("indexCodes", indexCodes.toArray());
        jsonBody.put("status", "1");
        jsonBody.put("includeSubNode", "1");
        String body = jsonBody.toString();
        //传参调用方法
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);
        //返回结果
        return result;
    }

    /*
     * @Description: 获取监控点编号进行云台操作
     * @author: Orange
     * @Param: int
     * @Return: java.lang.String
     * @Date: 2020/11/17 10:32
     */
    public static String getptzsInfo(String cameraIndexCode,String command,String speed,String presetIndex) {

        //获取摄像头列表路径
        final String getCameraList = ARTEMIS_PATH + "/api/video/v1/ptzs/controlling";

        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getCameraList);
            }
        };
        //定义提交类型
        String contentType = "application/json";
        //参数装换
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("cameraIndexCode", cameraIndexCode);
        if(command.toUpperCase().equals("STOP")){
            jsonBody.put("action", 1);
            jsonBody.put("command", "LEFT");
        }else{
            jsonBody.put("action", 0);
            jsonBody.put("command", command);
        }
        jsonBody.put("speed", speed);
        jsonBody.put("presetIndex", presetIndex);
        String body = jsonBody.toString();
        //传参调用方法
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);
        //返回结果
        return result;
    }

    public static String getManualCapture(String cameraIndexCode){
        //获取摄像头列表路径
        final String getCameraList = ARTEMIS_PATH + "/api/video/v1/manualCapture";

        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getCameraList);
            }
        };
        //定义提交类型
        String contentType = "application/json";
        //参数装换
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("cameraIndexCode", cameraIndexCode);
        String body = jsonBody.toString();
        //传参调用方法
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);
        //返回结果
        return result;
    }

    //读取摄像头抓取的图片记录
    public static String getManualCapturePhotoRecord(String cameraIndexCode,String startTime,String endTime){
        //获取摄像头列表路径
        final String getCameraList = ARTEMIS_PATH + "/api/video/v1/pictureInfos";

        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getCameraList);
            }
        };
        //定义提交类型
        String contentType = "application/json";
        //参数装换
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("pageNo",1);
        jsonBody.put("pageSize",100);
//        JsonArray cameraIndexCodes = new JsonArray();
        List<String> cameraIndexCodes=new ArrayList<>();
        cameraIndexCodes.add(cameraIndexCode);
//        cameraIndexCodes.add(cameraIndexCode.toString());
        jsonBody.put("cameraIndexCodes",cameraIndexCodes);
        jsonBody.put("startTime",startTime);
        jsonBody.put("endTime",endTime);
        jsonBody.put("netProtocol", "http");
        jsonBody.put("sort",0);
        jsonBody.put("compressRatio",50);
        System.out.println("jsonBody参数："+jsonBody);

        String body = jsonBody.toString();
        //传参调用方法
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);
        //返回结果
        return result;
    }
}
