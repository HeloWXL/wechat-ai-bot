package wx.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname WxBotConfig
 * @Description TODO 微信配置文件
 * @Date 2023/3/24 09:33
 * @Created by wangxianlin
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wxbot")
public class WxBotConfig {

    /**
     * appid
     */
    String appid;
    /**
     * token
     */
    String token;
    /**
     * encodingAESKey
     */
    String encodingAESKey;
}
