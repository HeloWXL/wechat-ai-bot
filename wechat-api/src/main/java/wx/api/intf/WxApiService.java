package wx.api.intf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import wx.api.config.WxBotConfig;
import wx.api.util.HttpClientUtil;
import wx.redis.util.RedisUtil;


import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;

import static wx.api.util.ConstantUtil.*;


@Component
public class WxApiService {

    public static Logger logger = LoggerFactory.getLogger(WxApiService.class);

    @Resource
    WxBotConfig wxConfig;

    @Resource
    RedisUtil redisUtil;


    /**
     * 获取SIGN
     *
     * @return
     */
    public String getSign(String userId) {
        // 从redis 取出 sign ，即判断缓存是否存在 sign，不存在时再去通过接口获取
        String sign = redisUtil.getAccessToken(ACCESS_SIGN_KEY+userId);
        if (sign != null) {
            return sign;
        } else {
            String url = GET_SIGN + wxConfig.getToken();
            JSONObject params = new JSONObject(1);
            params.put("userid", userId);
            String res = null;
            try {
                res = HttpClientUtil.jsonPost(url, params.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("获取SIGN出错");
            }
            JSONObject accessTokenRes = JSON.parseObject(res);
            String token = accessTokenRes.getString("signature");
            Long expires = accessTokenRes.getLong("expiresIn");
            // 将token 存储在redis 并设置过期时间，以便下次获取
            redisUtil.setAccessToken(ACCESS_SIGN_KEY+userId, token, expires);
            return token;
        }
    }

    /**
     * 智能对话
     *
     * @param userId
     * @return
     */
    public JSONObject aiChat(String userId, String question) {
        String url = AI_CHAT_URL + wxConfig.getToken();
        String sign = getSign(userId);
        JSONObject params = new JSONObject(2);
        params.put("signature", sign);
        params.put("query", question);
        String res = null;
        try {
            res = HttpClientUtil.jsonPost(url, params.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("调用智能会话接口出错");
        }
        JSONObject jsonObject = JSON.parseObject(res);
        JSONArray msgArray = jsonObject.getJSONArray("msg");
        JSONObject resJsonObject = msgArray.getJSONObject(0);
        return resJsonObject;
    }
}
