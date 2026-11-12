package api.tools;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

public class FlaskApiUtil {
    private static final RestTemplate restTemplate = new RestTemplate();

    public static Map<String, Object> fetchPrecipData(String FLASK_API_URL,String area) {
        // 构造请求体
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("area", area);

        System.out.println("FLASK_API_URL " +FLASK_API_URL);
        // 发送 POST 请求
        ResponseEntity<Map> response = restTemplate.postForEntity(
                FLASK_API_URL,
                requestBody,
                Map.class
        );

        // 返回响应数据
        return response.getBody();
    }
}