package wx.redis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Classname RedisUtil
 * @Description TODO
 * @Date 2023/3/24 12:48
 * @Created by wangxianlin
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 将accessToken缓存指redis
     *
     * @param tokenKey
     * @param accessToken
     * @param timeout
     */
    public void setAccessToken(String tokenKey, String accessToken, Long timeout) {
        this.redisTemplate.opsForValue().set(tokenKey, accessToken, timeout, TimeUnit.SECONDS);
    }

    /**
     * 根据Key获取AccessToken
     *
     * @param tokenKey
     * @return
     */
    public String getAccessToken(String tokenKey) {
        if (existAccessToken(tokenKey)) {
            return (String) this.redisTemplate.opsForValue().get(tokenKey);
        }
        return null;
    }

    public boolean existAccessToken(String tokenKey) {
        return this.redisTemplate.hasKey(tokenKey);
    }


}
