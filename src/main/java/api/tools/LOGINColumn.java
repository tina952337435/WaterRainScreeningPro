package api.tools;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LOGINColumn {
    private String username;
    private String password;
    private String platform;
    private String code;
    private String phoneNumber;

    /*用来区分使用 哪个小程序key （1 公司小程序、 2 昆山小程序）  默认为1 */
    private String type;
}