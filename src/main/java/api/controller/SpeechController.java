package api.controller;

//import api.pojo.ksdata.SYS_SPEECHBASEPojo;
//import api.server.swzzmxsq.SpeechServer;
//import api.server.swzzmxsq.SpeechTencentService;
import api.tools.ColumnName;
import api.tools.SpeechTencentUtils;
import api.tools.SpeechUtils;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Speech")
public class SpeechController {
    @Autowired
    private SpeechUtils speechUtils;

//    @Autowired
//    private SpeechServer speechServer;

//    @Autowired
//    private SpeechTencentService speechTencentServer;

    @RequestMapping(value = "/getSpeechResult",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map getSpeechResult(@RequestPart("wavfile") MultipartFile wavfile) throws IOException {
        String speechResult = "";
        try {
            String token = speechUtils.createAccessToken();
            speechResult = speechUtils.getSpeechResult(wavfile.getBytes(),token);
            if(!speechResult.equals("")){
                return new ObjectMapper().readValue(speechResult,Map.class);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new ObjectMapper().readValue(speechResult,Map.class) ;
    }

    @RequestMapping("/getSpeechWordResult")
    public Map getSpeechWordResult(@RequestBody ColumnName param){
        Map wordResult = new HashMap();
        String text = "";
        if(null != param.getDatasource()){
            text = param.getDatasource();
        }
        try {
            String token = speechUtils.createAccessToken();
            wordResult = speechUtils.getSpeechWordResult(text,token);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return wordResult;
    }

    @RequestMapping("/getSpeechAndWord")
    public Map getSpeechAndWord(@RequestPart("wavfile") MultipartFile wavfile) {

        String token = null;
        Map map = new HashMap<>();

        Map wordResult = new HashMap<>();
        try {
            token = "24.a9bedd33a87f6e3c230238fbeec0c0c8.2592000.1696405813.282335-38613959";//sample.createAccessToken();
            List<String> textArray = new ArrayList<>();
            //根据语音 识别为  文字
            String speechResult = speechUtils.getSpeechResult(wavfile.getBytes(),token);
            if(!speechResult.equals("")){
                wordResult = new ObjectMapper().readValue(speechResult, Map.class);
            }
            if(null != wordResult && Integer.valueOf(wordResult.get("err_no").toString()) == 0){
                Object result = wordResult.get("result");
                if(null != result) {
                    textArray = (List<String>) result;
                    StringBuilder text = new StringBuilder();
                    for (String object : textArray) {
                        text.append(object);
                    }
                    try {
                        //语音解译为关键字，进行数据匹配
                        if ("" != text.toString().trim()) {
                            wordResult = speechUtils.getSpeechWordResult(text.toString(), token);
//                            wordResult = speechServer.getSpeechForData(wordResult);
                        }
                        System.out.println(" wordResult Res = " + JSON.toJSONString(wordResult));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<Map<String ,Object>> mList = new ArrayList<>();
        mList.add(wordResult);
        map.put("result",mList);
        map.put("total",mList.size());
        map.put("success","true");
        map.put("Message","操作成功");

        return map;
    }

//    @PostMapping("/getSpeechKeyByVoice")
//    public Map getSpeechKeyByVoice(@RequestPart(value = "wavfile", required = false) MultipartFile wavfile,@RequestParam(value = "pathname", required = false) String pathname,@RequestParam(value ="key", required = false) String key) {
//
//        String token = null;
//        Map map = new HashMap<>();
//
//        Map wordResult = new HashMap<>();
//        List<SYS_SPEECHBASEPojo> mList = new ArrayList<>();
//        try {
//            System.out.println(pathname);
//            if(!StringUtils.isEmpty(pathname) && pathname.equals("tx")){
//                String v = "";
//                if(!StringUtils.isEmpty(key)){
//                    v = key;
//                    try {
//                        wordResult = new ObjectMapper().readValue(v, Map.class);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }else{
//                    //腾讯api（语音转文字）
//                    wordResult = SpeechTencentUtils.getSpeechResult(wavfile.getBytes());
//                }
//                if(null != wordResult && Integer.valueOf(wordResult.get("code").toString()) == 0){
//                    //解译所有关键字、关键词
//                    List<Map<String,Object>> mapListWord =  SpeechTencentUtils.getSpeechWordResult(wordResult);
//                    if(null != mapListWord && mapListWord.size() > 0){
//                        //（关键字、关键词） 进行操作项匹配
//                        mList = speechTencentServer.getSpeechData(mapListWord);
//                    }
//                }
//            }else{
//                //百度api
//                token = SpeechUtils.createAccessToken();
//                List<String> textArray = new ArrayList<>();
//                //根据语音 识别为 文字
//                String speechResult = speechUtils.getSpeechResult(wavfile.getBytes(),token);
//                if(!speechResult.equals("")){
//                    wordResult = new ObjectMapper().readValue(speechResult, Map.class);
//                }
//                System.out.println("wordResult = "+ wordResult);
//                //识别成功
//                if(null != wordResult && Integer.valueOf(wordResult.get("err_no").toString()) == 0){
//                    Object result = wordResult.get("result");
//                    if(null != result) {
//                        textArray = (List<String>) result;
//                        StringBuilder text = new StringBuilder();
//                        for (String object : textArray) {
//                            text.append(object);
//                        }
//                        try {
//                            //语音解译为关键字，进行数据匹配
//                            if ("" != text.toString().trim()) {
//                                //文字拆解为 （关键字、关键词）
//                                wordResult = speechUtils.getSpeechWordResult(text.toString(), token);
//
//                                //（关键字、关键词） 进行操作项匹配
//                                mList = speechServer.getSpeechData(wordResult);
//                            }
//                            System.out.println(" wordResult Res = " + JSON.toJSONString(wordResult));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        map.put("result",mList);
//        map.put("total",mList.size());
//        map.put("success","true");
//        map.put("Message","操作成功");
//
//        return map;
//    }

//    @RequestMapping("/getSpeechKey")
//    public Map getSpeechKey(@RequestBody ColumnName param) {
//        Map map = new HashMap<>();
//
//        String token = "24.a9bedd33a87f6e3c230238fbeec0c0c8.2592000.1696405813.282335-38613959";//sample.createAccessToken();
//        String key = "";
//        List<SYS_SPEECHBASEPojo> mList = new ArrayList<>();
//        if(null != param.getKey()){
//            String text = param.getKey();
//            Map wordResult = new HashMap<>();
//            try {
//                //文字拆解为 （关键字、关键词）
//                wordResult = speechUtils.getSpeechWordResult(text.toString(), token);
//
//                //（关键字、关键词） 进行操作项匹配
//                mList = speechServer.getSpeechData(wordResult);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        map.put("result",mList);
//        map.put("total",mList.size());
//        map.put("success","true");
//        map.put("Message","操作成功");
//        return map;
//    }

}
