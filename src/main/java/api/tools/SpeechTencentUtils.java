package api.tools;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.SpeechClient;
import com.tencent.asr.model.Credential;
import com.tencent.asr.model.FlashRecognitionRequest;
import com.tencent.asr.model.FlashRecognitionResponse;
import com.tencent.asr.service.FlashRecognizer;
import com.tencent.core.utils.ByteUtils;
import com.tencent.core.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 腾讯云语音配置Bean，由Spring注入读取yml
 */
@Component
public class TencentSpeechConfig {
    public static String APPID;
    public static String SECRET_ID;
    public static String SECRET_KEY;

    @Value("${tencent.speech.app-id}")
    public void setAppId(String appId) {
        APPID = appId;
    }
    @Value("${tencent.speech.secret-id}")
    public void setSecretId(String secretId) {
        SECRET_ID = secretId;
    }
    @Value("${tencent.speech.secret-key}")
    public void setSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }
}

public class SpeechTencentUtils {

    /*
     * 语音解译为文字
     * */
    public static Map getSpeechResult(byte [] b) {
        Credential credential = Credential.builder()
                .secretId(TencentSpeechConfig.SECRET_ID)
                .secretKey(TencentSpeechConfig.SECRET_KEY)
                .build();
        FlashRecognizer recognizer = SpeechClient.newFlashRecognizer(TencentSpeechConfig.APPID, credential);
        String obj = runOnce(recognizer,b);
        Map map = null;
        try{
            map = new ObjectMapper().readValue(obj.toString(), Map.class);
        }catch (IOException exx){
        }
        return map;
    }
    /*
     * 解译关键字、关键词
     * */
    public static List<Map<String,Object>> getSpeechWordResult(Map mLexer) {
        List<Map<String,Object>> map = new ArrayList<>();
        if(null != mLexer && null != mLexer.get("flash_result")){
            List<Map<String,Object>> maps = (List<Map<String, Object>>) mLexer.get("flash_result");
            for (Map<String,Object> result : maps) {
                if(null != result.get("sentence_list")){
                    List<Map<String,Object>> mapsListSentence = (List<Map<String, Object>>)result.get("sentence_list");
                    if(null != mapsListSentence){
                        for (Map<String,Object> sentence : mapsListSentence) {
                             if(null != sentence.get("word_list").toString()){
                                 List<Map<String,Object>> mapsListWord = (List<Map<String, Object>>)sentence.get("word_list");
                                 if(null != mapsListWord) {
                                     for (Map<String,Object> key : mapsListWord) {
                                         if(map.contains(key.get("word")) == false)
                                            map.add(key);
                                     }
                                 }
                             }
                        }
                    }
                }
            }
        }
        return map;
    }

    public static String runOnce(FlashRecognizer recognizer,byte [] b) {
        byte[] data = null;
        if(null !=b){
            data = b;
        }else{
            String relativerPath = System.getProperty("user.dir")+ File.separator +"test_wav/16k/16k.wav";
            System.out.println(relativerPath);
            ClassLoader classLoader = SpeechTencentUtils.class.getClassLoader();
            File file =new File(classLoader.getResource("test_wav/16k/16k.wav").getFile());
            String relativePath = file.getAbsolutePath();
            System.out.println(classLoader.getResource("test_wav/16k/16k.wav").getFile());
            System.out.println(relativePath);
            relativePath = "D:\\work\\UploadDoc\\test_wav\\16k\\16k.wav";
            data = ByteUtils.inputStream2ByteArray(relativePath);
        }

        //传入识别语音数据同步获取结果
        FlashRecognitionRequest recognitionRequest = FlashRecognitionRequest.initialize();
        recognitionRequest.setEngineType("16k_zh");
        recognitionRequest.setFirstChannelOnly(1);
        recognitionRequest.setVoiceFormat("wav");
        recognitionRequest.setSpeakerDiarization(0);
        recognitionRequest.setFilterDirty(0);
        recognitionRequest.setFilterModal(0);
        recognitionRequest.setFilterPunc(0);
        recognitionRequest.setConvertNumMode(1);
        recognitionRequest.setWordInfo(1);
        FlashRecognitionResponse response = recognizer.recognize(recognitionRequest, data);

        System.out.println(JsonUtil.toJson(response));
        return response.toString();
    }

    public static void main(String[] args) throws IOException {
        // 注意：main方法直接跑的时候，Spring没启动，配置注入不生效，本地main测试可以临时写密钥，上线必须走yml
        boolean once = false;
    }
}
