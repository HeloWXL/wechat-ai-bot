package wx.api.util;

/**
 * @Classname ConstantUtil
 * @Description TODO
 * @Date 2023/3/24 09:23
 * @Created by wangxianlin
 */
public class ConstantUtil {
    /**
     * 缓存AccessToken的KEY
     */
    public final static String ACCESS_SIGN_KEY = "ACCESS_SIGN_KEY::";

    /**
     * 获取signature接口 +{TOKEN}
     */
    public final static String GET_SIGN = "https://chatbot.weixin.qq.com/openapi/sign/";

    /**
     * 智能对话接口
     */
    public final static String AI_CHAT_URL = "https://chatbot.weixin.qq.com/openapi/aibot/";

}
